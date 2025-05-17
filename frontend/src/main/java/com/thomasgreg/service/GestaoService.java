package com.thomasgreg.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.primefaces.model.file.UploadedFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasgreg.auth.controller.LoginController;
import com.thomasgreg.auth.model.TokenManager;
import com.thomasgreg.controller.MensagemController;
import com.thomasgreg.dto.ClienteDTO;
import com.thomasgreg.http.ClienteHTTP;
import com.thomasgreg.util.Page;

@RequestScoped
public class GestaoService {
	
	@Inject 
	private ClienteHTTP clienteHTTP;
	
	@Inject
	private MensagemController msg;

	@Inject
	private LoginController loginController;

	public List<ClienteDTO> listarClientes() {
	    Response response = null;
	    TokenManager tokenManager = loginController.getTokenManager();
	    try {
	        response = clienteHTTP
	            .target("/clientes/")
	            .queryParam("page", 0)
	            .queryParam("size", 100)
	            .request(MediaType.APPLICATION_JSON)
	            .header("Authorization", "Bearer " + tokenManager.getToken())
	            .get();
	        if (response.getStatus() == 200) {
	            return converteResponseEmPageClienteDTO(response);
	        } else if (response.getStatus() == 500) {
	            loginController.tokenExpirou();
	        } else {
	        	msg.addMensagemErro("Erro", "Backend indisponível: HTTP " + response.getStatus());
	        }
	    } finally {
	        response.close();
	    }
	    return null;
	}

	public void salvarCliente(ClienteDTO cliente, UploadedFile logotipo) {
	    Response response = null;
	    TokenManager tokenManager = loginController.getTokenManager();
	    try {
		    MultipartFormDataOutput multipart = montaMultipartData(cliente, logotipo);
		    Entity<MultipartFormDataOutput> corpo = Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA);
	        response = clienteHTTP
	            .target("/clientes/")
	            .request()
	            .header("Authorization", "Bearer " + tokenManager.getToken())
	            .post(corpo);
	        if (response.getStatus() == 201) {
	            msg.addMensagemInfo("Sucesso", "Cliente salvo com sucesso!");
	        } else if (response.getStatus() == 409) {
	            msg.addMensagemAviso("Aviso", "Já existe um cliente cadastrado com esse email!");
	        } else {
	            String detalhe = response.hasEntity() ? response.readEntity(String.class) : "";
                msg.addMensagemErro("Falha", "Erro ao salvar cliente (HTTP " + response.getStatus() + ") " + detalhe);
	        }
	    } catch (Exception e) {
	    	msg.addMensagemErro("Falha", "Erro ao ler arquivo de imagem.");
		} finally {
	        response.close();
	    }
	}

	public void deletarClientePorId(String clienteId) {
	    Response response = null;
	    TokenManager tokenManager = loginController.getTokenManager();
	    try {
	        response = clienteHTTP
	                .target("/clientes/{id}")
	                .resolveTemplate("id", clienteId)
	                .request()
	                .header("Authorization", "Bearer " + tokenManager.getToken())
	                .delete();
	        if (response.getStatus() == 204) {
	        	msg.addMensagemInfo("Sucesso", "Cliente excluído com sucesso!");
	        } else if (response.getStatus() == 500) {
	            loginController.tokenExpirou();
	        } else {
	        	msg.addMensagemErro("Falha", "Erro ao excluir cliente.");
	        }
	    } finally {
	        response.close();
	    }
	}

	private MultipartFormDataOutput montaMultipartData(ClienteDTO cliente, UploadedFile logotipo)
			throws JsonProcessingException, IOException {
		String clienteJson = converteClienteEmJson(cliente);
		MultipartFormDataOutput multipartData = new MultipartFormDataOutput();
		multipartData.addFormData("cliente", clienteJson, MediaType.APPLICATION_JSON_TYPE);
		InputStream logotipoInput = logotipo.getInputStream();
		multipartData.addFormData("logotipo", logotipoInput, MediaType.valueOf(logotipo.getContentType()),
				logotipo.getFileName());
		return multipartData;
	}

	private List<ClienteDTO> converteResponseEmPageClienteDTO(Response response) {
		String jsonString = response.readEntity(String.class);
		ObjectMapper mapper = new ObjectMapper();
		Page<ClienteDTO> pageResponse = null;
		try {
			pageResponse = mapper.readValue(jsonString, new TypeReference<Page<ClienteDTO>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pageResponse.getContent();
	}

	private String converteClienteEmJson(ClienteDTO cliente) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String clienteJson = objectMapper.writeValueAsString(cliente);
		return clienteJson;
	}

}