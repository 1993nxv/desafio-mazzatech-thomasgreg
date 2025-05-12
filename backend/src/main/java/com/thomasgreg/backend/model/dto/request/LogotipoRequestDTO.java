package com.thomasgreg.backend.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogotipoRequestDTO {

    private Long id;
    private byte[] logotipo;
    private String tipoArquivo;
    private ClienteRequestDTO cliente;
     
}
