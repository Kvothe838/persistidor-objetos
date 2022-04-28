package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="clase")
public class Clase {
    @Id
    private int id;
    @Column(name="nombre")
    private String nombre;
    @OneToMany(mappedBy = "id_clase")
    private List<Atributo> atributos;
}
