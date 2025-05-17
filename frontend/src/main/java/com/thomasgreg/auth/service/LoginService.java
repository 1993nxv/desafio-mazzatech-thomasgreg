package com.thomasgreg.auth.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thomasgreg.auth.model.dto.AuthRequestDTO;
import com.thomasgreg.auth.model.dto.AuthResponseDTO;
import com.thomasgreg.controller.MensagemController;
import com.thomasgreg.http.ClienteHTTP;

@ApplicationScoped
public class LoginService {

    @Inject
    private ClienteHTTP clienteHTTP;
    
    @Inject
	private MensagemController msg;

    public AuthResponseDTO autenticar(AuthRequestDTO authRequest) {
    	Response response = null;
        try {
        	 response = clienteHTTP
                    .target("/auth")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(authRequest, MediaType.APPLICATION_JSON));
            if (response.getStatus() == 200) {
            	msg.addMensagemInfo("Usuário autenticado", "Seja bem vindo(a)!");
                return response.readEntity(AuthResponseDTO.class);
            } else {
                msg.addMensagemErro("Login inválido", "Usuário ou senha incorretos.");
            }
        } finally {
            response.close();
        }
        return null;
    }
}