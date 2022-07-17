package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name="valor_atributo")
public class ValorAtributo {
    @Id
    @GeneratedValue(strategy = TABLE)
    @EqualsAndHashCode.Exclude
    private Integer id;
//    @ManyToOne
//    private AtributoInstancia atributoInstancia;
    //para collections
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ValorAtributo> valorAtributoList;
    //para datos basicos
    @Column(name="valor")
    private String valor;
    //para objetos complejos
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Instancia instancia;
}
