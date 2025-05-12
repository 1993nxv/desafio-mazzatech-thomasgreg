package com.thomasgreg.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomasgreg.backend.model.Logotipo;

public interface LogotipoRepository extends JpaRepository<Logotipo, Long> {

    Optional<Logotipo> findByClienteId(Long clienteId);

    @Modifying
    @Query("UPDATE Logotipo l SET l.logotipo = :logotipo, l.tipoArquivo = :tipoArquivo WHERE l.cliente.id = :clienteId")
    void updateByClienteId(@Param("clienteId") Long clienteId, @Param("logotipo") byte[] logotipo, @Param("tipoArquivo") String tipoArquivo);

}
