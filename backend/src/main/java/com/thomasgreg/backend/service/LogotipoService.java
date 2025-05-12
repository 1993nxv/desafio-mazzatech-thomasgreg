package com.thomasgreg.backend.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.model.Logotipo;
import com.thomasgreg.backend.repository.LogotipoRepository;

@Service
public class LogotipoService {

    @Autowired
    private LogotipoRepository logotipoRepository;

    public Logotipo findByClienteId(Long clienteId) {
        Logotipo logotipo = logotipoRepository.findById(clienteId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logotipo do cliente com o id:"+clienteId+" não encontrado")
        );
        return logotipo;
    }

    @Transactional
    public void save(Logotipo logotipo, Cliente cliente) {
        logotipo.setCliente(cliente);
        logotipoRepository.save(logotipo);
    }

}
