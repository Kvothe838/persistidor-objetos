package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clase")
public class Clase {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @ManyToOne
    private Session session;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atributo> atributos;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instancia> instancias;

    public void agregarInstanciaPrincipal(Instancia instanciaPrincipal) {
        if(this.instancias == null){
            this.instancias = new ArrayList<>();
        }

        this.instancias.add(instanciaPrincipal);
    }
}
