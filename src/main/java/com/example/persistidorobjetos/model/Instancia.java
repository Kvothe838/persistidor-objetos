package com.example.persistidorobjetos.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="instancia")
public class Instancia {
    @Id
    private int id;
    @ManyToOne
    private Clase clase;
    @ManyToOne
    private Session session;
    @OneToMany
    private List<AtributoInstancia> atributos;
}
