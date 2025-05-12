package com.thomasgreg.backend.controller;

import java.io.IOException;
import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.model.Logotipo;
import com.thomasgreg.backend.model.Logradouro;
import com.thomasgreg.backend.model.dto.request.ClienteRequestDTO;
import com.thomasgreg.backend.model.dto.request.LogradouroRequestDTO;
import com.thomasgreg.backend.model.dto.response.ClienteLogotipoResponseDTO;
import com.thomasgreg.backend.model.dto.response.ClienteResponseDTO;
import com.thomasgreg.backend.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteService.findAll(pageable);

        Page<ClienteResponseDTO> clientesDTOs = clientes.map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class));

        return ResponseEntity.ok().body(clientesDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok().body(modelMapper.map(cliente, ClienteResponseDTO.class));
    }

    @GetMapping("/{clienteId}/logotipo")
    public ClienteLogotipoResponseDTO findLogotipoByClienteId(@PathVariable Long clienteId) {
        return modelMapper.map(clienteService.findLogotipoByClienteId(clienteId), ClienteLogotipoResponseDTO.class);
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @RequestPart(name = "cliente", required = true) String clienteJson,
            @RequestPart(name = "logotipo", required = false) MultipartFile logotipo) throws IOException {

        Cliente clienteRequest = mapper.readValue(clienteJson,  Cliente.class);
        Logotipo logotipoRequest = new Logotipo();
        if (logotipo != null) {
            logotipoRequest.setLogotipo(logotipo.getBytes());
            logotipoRequest.setTipoArquivo(logotipo.getContentType());
            clienteRequest.setLogotipo(logotipoRequest);
        }
         
        Long novoClienteId = clienteService.save(clienteRequest).getId();
        return ResponseEntity.created(URI.create("/clientes/" + novoClienteId)).build();
    }

    @PostMapping("/{clienteId}/logradouro")
    public ResponseEntity<?> saveLogradouro(@PathVariable Long clienteId, @Valid @RequestBody LogradouroRequestDTO logradouroRequest) {
        Logradouro logradouro = modelMapper.map(logradouroRequest, Logradouro.class);
        clienteService.saveLogradouro(clienteId, logradouro).getId();
        return ResponseEntity.created(URI.create("/clientes/"+clienteId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO clienteRequest) {
        Cliente cliente = modelMapper.map(clienteRequest, Cliente.class);
        clienteService.updateById(id, cliente);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{clienteId}/logotipo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateLogotipoByClienteId(
            @PathVariable Long clienteId,
            @RequestPart(name = "logotipo", required = true) MultipartFile logotipoRequest) throws IOException {
        
        Logotipo logotipo = new Logotipo();
        if (logotipoRequest != null) {
            logotipo.setLogotipo(logotipoRequest.getBytes());
            logotipo.setTipoArquivo(logotipoRequest.getContentType());
        }

        clienteService.updateLogotipoByClienteId(logotipo, clienteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
