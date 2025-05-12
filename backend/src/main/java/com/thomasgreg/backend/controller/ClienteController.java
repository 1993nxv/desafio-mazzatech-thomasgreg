package com.thomasgreg.backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.model.dto.response.ClienteResponseDTO;
import com.thomasgreg.backend.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteService.findAll(pageable);

        Page<ClienteResponseDTO> clientesDTOs = clientes.map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class));

        return ResponseEntity.ok().body(clientesDTOs);
    }
}
