package com.thomasgreg.dto;

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
public class ClienteLogotipoResponseDTO {

    private Long id;
    private byte[] logotipo;
    private String tipoArquivo;
    
}
