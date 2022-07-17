package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @GeneratedValue(strategy = TABLE)
    private int id;
    @ManyToOne
    private Clase clase;
    @ManyToOne(cascade = CascadeType.ALL)
    private Session session;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "instancia_id")
    private List<AtributoInstancia> atributos;
}
