package com.thomasgreg.service;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thomasgreg.auth.AuthRequestDTO;
import com.thomasgreg.auth.AuthResponseDTO;
import com.thomasgreg.config.AppConfig;

@RequestScoped
public class LoginService {

	private static final String BASE_URL = AppConfig.get("api.base.url");

	public AuthResponseDTO autenticar(AuthRequestDTO authRequest) {
		Client clientHTTP = ClientBuilder.newClient();
		WebTarget target = clientHTTP
				.target(BASE_URL)
				.path("/auth");
		Response response = target.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(authRequest, MediaType.APPLICATION_JSON));
		if (response.getStatus() == 200) {
			return response.readEntity(AuthResponseDTO.class);
		}
		return null;
	}
}