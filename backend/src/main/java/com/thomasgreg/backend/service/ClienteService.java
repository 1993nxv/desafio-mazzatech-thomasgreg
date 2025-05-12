package com.thomasgreg.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.model.Logotipo;
import com.thomasgreg.backend.model.Logradouro;
import com.thomasgreg.backend.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LogradouroService logradouroService;

    @Autowired
    private LogotipoService logotipoService;

    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente findById(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente com o id:"+id+" não encontrado")
        );
        return cliente;
    }

    public Logotipo findLogotipoByClienteId(Long clienteId) {
        findById(clienteId);
        return logotipoService.findByClienteId(clienteId);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        verificarEmailExistente(cliente.getEmail());

        List<Logradouro> logradouros = cliente.getLogradouros();
        Logotipo logotipo = cliente.getLogotipo();
        cliente.setLogradouros(null);
        cliente.setLogotipo(null);

        Cliente clienteSalvo = clienteRepository.save(cliente);

        logradouroService.saveAll(logradouros, clienteSalvo);
        logotipoService.save(logotipo, clienteSalvo);

        return clienteSalvo;
    }

    @Transactional
    public void updateById(Long id, Cliente cliente) {
        Cliente clienteAtual = findById(id);
        cliente.setId(id);
        if(!cliente.getEmail().equals(clienteAtual.getEmail())) {
            verificarEmailExistente(cliente.getEmail());
        }

        List<Logradouro> logradouros = cliente.getLogradouros();

        clienteRepository.updateById(id, cliente.getNome(), cliente.getEmail());
        logradouroService.updateAllById(logradouros, cliente);
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        clienteRepository.deleteById(id);
    }

    public void verificarEmailExistente(String email) {
        if (clienteRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um cliente cadastrado com este e-mail");
        }
    }

}
