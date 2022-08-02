package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import java.util.List;

import javax.persistence.*;

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
    @ManyToOne
    private ValorAtributo parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ValorAtributo> valorAtributoList;
    //para datos basicos
    @Column(name="valor")
    private String valor;
    //para objetos complejos
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Instancia instancia;
}
