package com.thomasgreg.auth.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.thomasgreg.auth.model.TokenManager;
import com.thomasgreg.auth.model.Usuario;
import com.thomasgreg.auth.model.dto.AuthRequestDTO;
import com.thomasgreg.auth.model.dto.AuthResponseDTO;
import com.thomasgreg.auth.service.LoginService;
import com.thomasgreg.controller.MensagemController;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = -8749448214470369140L;
	
	@Getter
	private AuthRequestDTO authRequest = new AuthRequestDTO();;

	@Inject @Getter @Setter
	private TokenManager tokenManager;
	
	@Inject @Getter @Setter
	private Usuario usuario;

	@Inject
	private LoginService loginService;
	
    @Inject
	private MensagemController msg;

	public void login() {
		AuthResponseDTO authResponse = new AuthResponseDTO();
		authResponse = loginService.autenticar(authRequest);
		if (authResponse != null && authResponse.getAccessToken() != null) {
			tokenManager.storeToken(authResponse.getAccessToken());
			usuario.setUsername(authResponse.getUser().getUsername());
			usuario.setRoles(authResponse.getUser().getRoles());
			redirecionaParaGestao();
		}
	}

	public void logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/frontend/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tokenExpirou() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		redirecionaLogin();
		msg.addMensagemAviso("Atenção","Você precisa fazer login novamente!");
	}

	public boolean existeUsuarioLogado() {
		return this.usuario.getUsername() != null;
	}
	
	private void redirecionaLogin() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/frontend/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void redirecionaParaGestao() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/frontend/pages/gestao/index.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
