package com.example.persistidorobjetos.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
