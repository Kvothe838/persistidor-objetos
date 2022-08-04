package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "clase")
public class Clase {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @ManyToOne
    private Session session;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atributo> atributos;
}
