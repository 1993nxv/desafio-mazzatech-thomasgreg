package com.thomasgreg.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.thomasgreg.auth.TokenManager;
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

    public String login() {
            authResponse = loginService.autenticar(authRequest);
            if (authResponse != null && authResponse.getAccessToken() != null) {
                tokenManager.storeToken(authResponse.getAccessToken());
                System.out.println(authResponse.toString());
                return "gerenciar?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido", "Usuário ou senha incorretos."));
                return null;
            }
    }

    public AuthRequestDTO getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(AuthRequestDTO authRequest) {
        this.authRequest = authRequest;
    }
}
