package com.thomasgreg.auth.model;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class TokenManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;

    public void storeToken(String token) {
        this.accessToken = token;
    }

    public String getToken() {
        return accessToken;
    }

    public void clearToken() {
        this.accessToken = null;
    }

    public boolean isAuthenticated() {
        return accessToken != null && !accessToken.isEmpty();
    }
}