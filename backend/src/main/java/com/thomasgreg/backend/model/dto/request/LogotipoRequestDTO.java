package com.thomasgreg.backend.model.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogotipoRequestDTO {

    private Long id;
    private MultipartFile logotipo;
    private String tipoArquivo;
    private ClienteRequestDTO cliente;
     
}
