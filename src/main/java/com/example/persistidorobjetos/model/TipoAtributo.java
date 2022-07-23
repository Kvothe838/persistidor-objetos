package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.GenerationType.TABLE;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name="tipo_atributo")
public class TipoAtributo {
    @Id
    @GeneratedValue(strategy = TABLE)
    @EqualsAndHashCode.Exclude
    private Integer id;
    @Column(name="nombre")
    private String nombre;
}
