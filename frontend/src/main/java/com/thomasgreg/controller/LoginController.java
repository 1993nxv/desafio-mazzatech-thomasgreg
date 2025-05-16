package com.thomasgreg.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.thomasgreg.auth.AuthRequestDTO;
import com.thomasgreg.auth.AuthResponseDTO;
import com.thomasgreg.auth.TokenManager;
import com.thomasgreg.auth.Usuario;
import com.thomasgreg.service.LoginService;

@Named
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = -8749448214470369140L;
	
	private AuthRequestDTO authRequest = new AuthRequestDTO();;

	@Inject
	private TokenManager tokenManager;
	
	@Inject
	private Usuario usuario;

	@Inject
	private LoginService loginService;

	public void login() {
		AuthResponseDTO authResponse = new AuthResponseDTO();
		authResponse = loginService.autenticar(authRequest);
		if (authResponse != null && authResponse.getAccessToken() != null) {
			tokenManager.storeToken(authResponse.getAccessToken());
			usuario.setUsername(authResponse.getUser().getUsername());
			usuario.setRoles(authResponse.getUser().getRoles());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Login realizado com sucesso", "Seja bem vindo!"));
			redirecionaParaGestao();
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Login inválido", "Usuário ou senha incorretos."));
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
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Você precisa fazer login novamente!"));
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
	
    public AuthRequestDTO getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(AuthRequestDTO authRequest) {
        this.authRequest = authRequest;
    }
    
	public TokenManager getTokenManager() {
		return tokenManager;
	}

}
