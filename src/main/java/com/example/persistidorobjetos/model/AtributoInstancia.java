package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name="atributo_instancia")
public class AtributoInstancia {
    @Id
    @GeneratedValue(strategy = TABLE)
    private Integer id;
    @ManyToOne
    private Atributo atributo;
    @ManyToOne
    private Instancia instancia;
    @OneToOne
    (orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private ValorAtributo valorAtributo;
}
