package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "atributo")
public class Atributo {
    @Id
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @ManyToOne
    private TipoAtributo tipoAtributo;
}
