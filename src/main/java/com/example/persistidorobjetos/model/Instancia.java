package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="clase")
public class Instancia {
    @Id
    private int id;
    @ManyToOne
    private Clase clase;
    @ManyToOne
    private Session session;
    @OneToMany(mappedBy = "id_instancia")
    private List<AtributoInstancia> atributos;
}