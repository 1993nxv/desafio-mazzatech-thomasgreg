package com.thomasgreg.backend.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteLogotipoResponseDTO {

    private Long id;
    private byte[] logotipo;
    private String tipoArquivo;
    
}
