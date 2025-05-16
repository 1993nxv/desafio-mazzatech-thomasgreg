package com.thomasgreg.auth;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthResponseDTO {
	
	private String accessToken;
    private String tokenType;
    private int expiresIn;
    private UsuarioDTO user;
    
}
