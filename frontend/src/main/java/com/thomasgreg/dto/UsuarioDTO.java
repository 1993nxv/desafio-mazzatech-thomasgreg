package com.thomasgreg.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioDTO {
	
    private String username;
    private List<String> roles;
    
}
