package com.thomasgreg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomasgreg.backend.model.Cliente;
import com.thomasgreg.backend.repository.custom.ClienteRepositoryCustom;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteRepositoryCustom {

    @SuppressWarnings("null")
    @Query("SELECT c FROM Cliente c")
    Page<Cliente> findAll(Pageable pageable);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Cliente c SET c.nome = :nome, c.email = :email WHERE c.id = :id")
    void updateById(@Param("id") Long id, @Param("nome") String nome, @Param("email") String email);

}
