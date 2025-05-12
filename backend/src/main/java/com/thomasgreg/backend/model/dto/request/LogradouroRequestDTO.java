package com.thomasgreg.backend.model.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogradouroRequestDTO {

    private Long id;

    @NotBlank(message = "O campo logradouro é obrigatório e não pode ser vazio")
    private String logradouro;

    private ClienteRequestDTO cliente;

}
