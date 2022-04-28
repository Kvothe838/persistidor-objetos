package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="atributo")
public class Atributo {
    @Id
    private int id;
    @Column(name="nombre")
    private String nombre;
    @ManyToOne
    private TipoAtributo tipoAtributo;
    @ManyToOne
    private Clase clase;
}
