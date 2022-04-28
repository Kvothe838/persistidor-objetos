package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="valor_atributo")
public class ValorAtributo {
    @Id
    private int id;
    @ManyToOne
    private AtributoInstancia atributoInstancia;
    @OneToMany(mappedBy = "id")
    private ArrayList<ValorAtributo> valorAtributoList;
    @Column(name="valor")
    private String valor;
}
