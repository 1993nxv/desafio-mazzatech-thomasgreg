package com.thomasgreg.backend.security.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequestDTO {

    private String username;
    private String password;
    
}
