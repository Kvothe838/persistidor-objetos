package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="atributo_instancia")
public class AtributoInstancia {
    @Id
    private int id;
    @ManyToOne
    private Atributo atributo;
    @ManyToOne
    private Instancia instancia;
}
