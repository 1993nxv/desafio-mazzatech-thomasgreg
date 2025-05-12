package com.thomasgreg.backend.model.dto.request;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {

    private Long id;

    @NotNull(message = "O nome é obrigatório")
    private String nome;

    @NotBlank
    @Email(message = "Use um email válido")
    private String email;

    private MultipartFile logotipo;

    @NotNull(message = "O logradouro é obrigatório")
    private List<LogradouroRequestDTO> logradouros;
    
}
