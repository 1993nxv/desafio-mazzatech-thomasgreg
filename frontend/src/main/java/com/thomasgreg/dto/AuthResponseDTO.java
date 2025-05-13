package com.thomasgreg.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
	
    private String accessToken;
    private String tokenType;
    private int expiresIn;
    private UsuarioDTO user;
    
}
