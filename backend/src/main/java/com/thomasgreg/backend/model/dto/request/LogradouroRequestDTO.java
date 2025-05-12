package com.thomasgreg.backend.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogradouroRequestDTO {

    private Long id;
    private String logradouro;
    private ClienteRequestDTO cliente;

}
