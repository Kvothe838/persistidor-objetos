package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tipo_atrbuto")
public class TipoAtributo {
    @Id
    private int id;
    @Column(name="nombre")
    private String nombre;
}
