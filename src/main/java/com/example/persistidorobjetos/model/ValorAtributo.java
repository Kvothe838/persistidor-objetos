package com.example.persistidorobjetos.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
    private List<ValorAtributo> valorAtributoList;
    @Column(name="valor")
    private String valor;
}
