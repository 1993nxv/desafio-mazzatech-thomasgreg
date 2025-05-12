package com.thomasgreg.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.model.Logradouro;
import com.thomasgreg.backend.repository.LogradouroRepository;

@Service
public class LogradouroService {

    @Autowired
    private LogradouroRepository logradouroRepository;

    public Logradouro findById(Long id) {
        Logradouro logradouro = logradouroRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logradouro com o id:"+id+" não encontrado")
        );
        return logradouro;
    }

    @Transactional
    public List<Logradouro> saveAll(List<Logradouro> logradouros, Cliente cliente) {
        logradouros.forEach(
            logradouro -> logradouro.setCliente(cliente)
        );
        List<Logradouro> logradourosSalvos = logradouroRepository.saveAll(logradouros);
        return logradourosSalvos;
    }
    
}
