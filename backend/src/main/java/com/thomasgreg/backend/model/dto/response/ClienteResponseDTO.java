package com.thomasgreg.backend.model.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private List<LogradouroResponseDTO> logradouros;

}
