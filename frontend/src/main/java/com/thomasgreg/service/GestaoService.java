package com.thomasgreg.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.primefaces.model.file.UploadedFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasgreg.auth.TokenManager;
import com.thomasgreg.config.AppConfig;
import com.thomasgreg.controller.LoginController;
import com.thomasgreg.dto.ClienteResponseDTO;
import com.thomasgreg.dto.PageResponseDTO;
import com.thomasgreg.model.Cliente;

@RequestScoped
public class GestaoService {

	private static final String BASE_URL = AppConfig.get("api.base.url");

	@Inject
	private LoginController loginController;

	public List<ClienteResponseDTO> listarClientes() {
		TokenManager tokenManager = loginController.getTokenManager();
		Client client = ClientBuilder.newClient();
		try {
			WebTarget target = client.target(BASE_URL).path("/clientes/").queryParam("page", 0).queryParam("size", 100);
			Response response = target.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + tokenManager.getToken()).get();
			if (response.getStatus() == 200) {
				String jsonString = response.readEntity(String.class);
				ObjectMapper mapper = new ObjectMapper();

				PageResponseDTO<ClienteResponseDTO> pageResponse = mapper.readValue(jsonString,
						new TypeReference<PageResponseDTO<ClienteResponseDTO>>() {
						});
				return pageResponse.getContent();
			} else {
				throw new RuntimeException("Erro na requisição: HTTP " + response.getStatus());
			}
		} catch (IOException e) {
			throw new RuntimeException("Falha ao processar JSON", e);
		} finally {
			client.close();
		}
	}

	public void salvarCliente(Cliente cliente, UploadedFile logotipo) throws Exception {
		TokenManager tokenManager = loginController.getTokenManager();
		ObjectMapper objectMapper = new ObjectMapper();
		String clienteJson = objectMapper.writeValueAsString(cliente);
		MultipartFormDataOutput multipartData = new MultipartFormDataOutput();
		multipartData.addFormData("cliente", clienteJson, MediaType.APPLICATION_JSON_TYPE);
		if (logotipo != null && logotipo.getSize() > 0) {
			InputStream logotipoInputStream = logotipo.getInputStream();
			multipartData.addFormData("logotipo", logotipoInputStream, MediaType.valueOf(logotipo.getContentType()),
					logotipo.getFileName());
		}
		ResteasyClient client = new ResteasyClientBuilder()
				.register(org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataWriter.class).build();
		ResteasyWebTarget target = client.target(BASE_URL).path("/clientes/");
		Entity<MultipartFormDataOutput> entity = Entity.entity(multipartData, MediaType.MULTIPART_FORM_DATA);
		try (Response response = target.request().header("Authorization", "Bearer " + tokenManager.getToken())
				.post(entity)) {
			if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
				String location = response.getHeaderString("Location");
				if (location != null && !location.isEmpty()) {
					String id = location.substring(location.lastIndexOf('/') + 1);
					System.out.println("Cliente criado com ID: " + id);
				} else {
					System.out.println("Cliente criado, mas sem cabeçalho Location.");
				}
			} else {
				String erro = response.readEntity(String.class);
				throw new RuntimeException("Erro ao salvar cliente: HTTP " + response.getStatus() + " - " + erro);
			}
		}
	}

	public void deletarClientePorId(String clienteId) throws Exception {
		TokenManager tokenManager = loginController.getTokenManager();
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(BASE_URL).path("/clientes/" + clienteId);
		try (Response response = target.request().header("Authorization", "Bearer " + tokenManager.getToken())
				.delete()) {
			if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
				System.out.println("Cliente com ID " + clienteId + " deletado com sucesso.");
			} else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
				throw new RuntimeException("Cliente não encontrado com ID: " + clienteId);
			} else {
				throw new RuntimeException("Erro ao deletar cliente: HTTP " + response.getStatus());
			}
		}
	}

}