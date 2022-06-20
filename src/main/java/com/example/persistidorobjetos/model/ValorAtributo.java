package com.example.persistidorobjetos.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="valor_atributo")
public class ValorAtributo {
    @Id
    private int id;
//    @ManyToOne
//    private AtributoInstancia atributoInstancia;
    //para collections
    @OneToMany(mappedBy = "id")
    private List<ValorAtributo> valorAtributoList;
    //para datos basicos
    @Column(name="valor")
    private String valor;
    //para objetos complejos
    @OneToOne
    private Instancia instancia;
}
