package com.thomasgreg.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataWriter;
import org.primefaces.model.file.UploadedFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasgreg.auth.TokenManager;
import com.thomasgreg.config.AppConfig;
import com.thomasgreg.controller.LoginController;
import com.thomasgreg.dto.ClienteDTO;
import com.thomasgreg.util.Page;

@RequestScoped
public class GestaoService {

	private static final String BASE_URL = AppConfig.get("api.base.url");

	@Inject
	private LoginController loginController;

	public List<ClienteDTO> listarClientes() {
		TokenManager tokenManager = loginController.getTokenManager();
		Client clienteHTTP = ClientBuilder.newClient();
		WebTarget target = clienteHTTP
				.target(BASE_URL)
				.path("/clientes/")
				.queryParam("page", 0)
				.queryParam("size", 100);
		Response response = target.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + tokenManager.getToken())
				.get();
		if (response.getStatus() == 200) {
			return converteResponseEmPageClienteResponseDTO(response);
		} else if (response.getStatus() == 500) {
			loginController.tokenExpirou();
		} else {
			throw new RuntimeException("Backend indisponivel");
		}
		return null;
	}

	public void salvarCliente(ClienteDTO cliente, UploadedFile logotipo) throws Exception {
		TokenManager tokenManager = loginController.getTokenManager();
		MultipartFormDataOutput multipartData = montaMultipartData(cliente, logotipo);
		ResteasyClient clienteHTTP = new ResteasyClientBuilder()
				.register(MultipartFormDataWriter.class)
				.build();
		ResteasyWebTarget target = clienteHTTP.target(BASE_URL).path("/clientes/");
		Entity<MultipartFormDataOutput> corpoRequisicao = Entity.entity(multipartData, MediaType.MULTIPART_FORM_DATA);
		Response response = target
				.request()
				.header("Authorization", "Bearer " + tokenManager.getToken())
				.post(corpoRequisicao);
		if (response.getStatus() == 409) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Já existe um cliente cadastrado com esse email!"));
		}else if(response.getStatus() == 201) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cliente salvo com sucesso!"));
		}else {
			String erro = response.readEntity(String.class);
			throw new RuntimeException("Erro ao salvar cliente: HTTP " + response.getStatus() + " - " + erro);
		}
	}

	public void deletarClientePorId(String clienteId) throws Exception {
		TokenManager tokenManager = loginController.getTokenManager();
		ResteasyClient clienteHTTP = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = clienteHTTP.target(BASE_URL).path("/clientes/" + clienteId);
		Response response = target
				.request()
				.header("Authorization", "Bearer " + tokenManager.getToken())
				.delete();
		if (response.getStatus() == 204) {
		} else if (response.getStatus() == 500) {
			loginController.tokenExpirou();
		} else {
			throw new RuntimeException("Erro ao deletar cliente: HTTP " + response.getStatus());
		}
	}

	private MultipartFormDataOutput montaMultipartData(ClienteDTO cliente, UploadedFile logotipo)
			throws JsonProcessingException, IOException {
		String clienteJson = converteClienteRequestEmJson(cliente);
		MultipartFormDataOutput multipartData = new MultipartFormDataOutput();
		multipartData.addFormData("cliente", clienteJson, MediaType.APPLICATION_JSON_TYPE);
		InputStream logotipoInput = logotipo.getInputStream();
		multipartData.addFormData("logotipo", logotipoInput, MediaType.valueOf(logotipo.getContentType()),
				logotipo.getFileName());
		return multipartData;
	}

	private List<ClienteDTO> converteResponseEmPageClienteResponseDTO(Response response) {
		String jsonString = response.readEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		Page<ClienteDTO> pageResponse = null;
		try {
			pageResponse = mapper.readValue(jsonString, new TypeReference<Page<ClienteDTO>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pageResponse.getContent();
	}

	private String converteClienteRequestEmJson(ClienteDTO cliente) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String clienteJson = objectMapper.writeValueAsString(cliente);
		return clienteJson;
	}

}