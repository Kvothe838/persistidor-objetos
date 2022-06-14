package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.GenerationType.TABLE;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tipo_atributo")
@Data
public class TipoAtributo {
    @Id
    @GeneratedValue(strategy = TABLE)
    private int id;
    @Column(name="nombre")
    private String nombre;
}
