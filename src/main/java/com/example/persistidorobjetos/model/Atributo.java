package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="atributo")
public class Atributo {
	@Id
	@GeneratedValue(strategy = TABLE)
	@EqualsAndHashCode.Exclude
    private int id;
    @Column(name="nombre")
    private String nombre;
    @ManyToOne
	@Cascade(CascadeType.ALL)
    private TipoAtributo tipoAtributo;
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
	@JoinColumn(name = "clase_id")
    private Clase clase;
}
