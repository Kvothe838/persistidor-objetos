package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "instancia")
@Data
public class Instancia {
    @Id
    private int id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValorAtributo> atributos;
    @Column(name = "principal")
    private boolean principal;
}
