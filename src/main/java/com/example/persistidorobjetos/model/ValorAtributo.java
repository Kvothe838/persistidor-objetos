package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "valorAtributo")
public class ValorAtributo {
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private int id;
    @Column(name="valor")
    private String valor;
}
