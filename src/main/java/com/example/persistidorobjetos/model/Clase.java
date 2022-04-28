package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="clase")
public class Clase {
    @Id
    private int id;
    @Column(name="nombre")
    private String nombre;
    @OneToMany(mappedBy = "id_clase")
    private List<Atributo> atributos;
}
