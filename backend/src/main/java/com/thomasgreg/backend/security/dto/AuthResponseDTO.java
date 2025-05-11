package com.thomasgreg.backend.security.dto;

import java.util.List;

public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UserInfo user;

    public AuthResponseDTO(String accessToken, long expiresIn, UserInfo user) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public static class UserInfo {
        private String username;
        private List<String> roles;

        public UserInfo(String username, List<String> roles) {
            this.username = username;
            this.roles = roles;
        }

        public String getUsername() {
            return username;
        }

        public List<String> getRoles() {
            return roles;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public UserInfo getUser() {
        return user;
    }
}
