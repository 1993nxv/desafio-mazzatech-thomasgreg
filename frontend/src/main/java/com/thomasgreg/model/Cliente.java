package com.thomasgreg.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Cliente {

    private Long id;
    private String nome;
    private String email;
    private Logotipo logotipo;
    private List<Logradouro> logradouros = new ArrayList<>();

}
