package com.thomasgreg.dto;

import java.util.List;

import org.primefaces.model.file.UploadedFile;

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
public class ClienteRequestDTO {

    private Long id;
    private String nome;
    private String email;
    private UploadedFile logotipo;
    private List<LogradouroRequestDTO> logradouros;
    
}
