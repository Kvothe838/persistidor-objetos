package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="tipoAtributo")
@Data
public class TipoAtributo {
    @Id
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @OneToOne
    private Clase clase;
}
