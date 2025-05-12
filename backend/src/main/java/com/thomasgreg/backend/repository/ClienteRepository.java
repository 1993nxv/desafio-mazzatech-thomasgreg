package com.thomasgreg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thomasgreg.backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @SuppressWarnings("null")
    @Query("SELECT c FROM Cliente c")
    Page<Cliente> findAll(Pageable pageable);

}
