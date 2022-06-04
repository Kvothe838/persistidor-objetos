package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tipo_atributo")
@Data
public class TipoAtributo {
    @Id @Generated
    private int id;
    @Column(name="nombre")
    private String nombre;
}
