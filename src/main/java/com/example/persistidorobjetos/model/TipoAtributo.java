package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tipoAtributo")
@Data
public class TipoAtributo {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @OneToOne
    private Clase clase;
    @OneToMany
    private List<ValorAtributo> valores;
}
