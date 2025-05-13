package com.thomasgreg.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.thomasgreg.auth.TokenManager;
import com.thomasgreg.auth.Usuario;
import com.thomasgreg.dto.AuthRequestDTO;
import com.thomasgreg.dto.AuthResponseDTO;
import com.thomasgreg.service.LoginService;

@Named
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = -8749448214470369140L;

    private AuthRequestDTO authRequest = new AuthRequestDTO();
    private AuthResponseDTO authResponse;

    @Inject
    private LoginService loginService;
    
    @Inject
    private TokenManager tokenManager;
    
    @Inject
    private Usuario usuario;

    public String login() {
            authResponse = loginService.autenticar(authRequest);
            if (authResponse != null && authResponse.getAccessToken() != null) {
                tokenManager.storeToken(authResponse.getAccessToken());
                
                usuario.setUsername(authResponse.getUser().getUsername());
                usuario.setRoles(authResponse.getUser().getRoles());
                
                try {
	                FacesContext.getCurrentInstance()
	                	.getExternalContext().redirect("/frontend/pages/gestao/index.xhtml");
	                return "";
                } catch (Exception e) {
                	return "";
				}
            } else {
                return null;
            }
    }
    
    public void logout() throws IOException {
    	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    	FacesContext.getCurrentInstance()
    		.getExternalContext().redirect("/frontend/");
    }
    
    public boolean existeUsuarioLogado() {
    	return this.usuario.getUsername() != null;
    }
    
    public Usuario getUsuario() {
    	return this.usuario;
    }
    
    public TokenManager getTokenManager() {
    	return this.tokenManager;
    }

    public AuthRequestDTO getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(AuthRequestDTO authRequest) {
        this.authRequest = authRequest;
    }
    
}
