package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="atributo_instancia")
@Data
public class AtributoInstancia {
    @Id
    @GeneratedValue(strategy = TABLE)
    private int id;
    @ManyToOne
    private Atributo atributo;
    @ManyToOne
    private Instancia instancia;
    @OneToOne
    (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private ValorAtributo valorAtributo;
}
