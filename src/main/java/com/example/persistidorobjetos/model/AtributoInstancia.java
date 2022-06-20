package com.example.persistidorobjetos.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    private int id;
    @ManyToOne
    private Atributo atributo;
    @ManyToOne
    private Instancia instancia;
    @OneToOne
    private ValorAtributo valorAtributo;
}
