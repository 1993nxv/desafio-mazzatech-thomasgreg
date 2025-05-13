package com.thomasgreg.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.enterprise.context.RequestScoped;

import org.jboss.logging.Logger;

import com.thomasgreg.config.AppConfig;
import com.thomasgreg.dto.AuthRequestDTO;
import com.thomasgreg.dto.AuthResponseDTO;

@RequestScoped
public class LoginService {
    
    private static final Logger logger = Logger.getLogger(LoginService.class.getName());
    private static final String BASE_URL = AppConfig.get("api.base.url");

    public AuthResponseDTO autenticar(AuthRequestDTO authRequestDTO) {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget target = client.target(BASE_URL).path("/auth");
            Response response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(authRequestDTO, MediaType.APPLICATION_JSON));

            logger.info("Resposta da API: HTTP " + response.getStatus());
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(AuthResponseDTO.class);
            } else {
                String errorResponse = response.readEntity(String.class);
                logger.debug("Erro na API: " + errorResponse);
                throw new RuntimeException("API retornou erro: " + response.getStatus() + " - " + errorResponse);
            }
        } catch (Exception e) {
            logger.debug("Erro na comunicação com a API: " + e.getMessage());
            throw new RuntimeException("Falha na autenticação: " + e.getMessage(), e);
        } finally {
            client.close();
        }
    }
}