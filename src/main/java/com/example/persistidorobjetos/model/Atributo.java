package com.example.persistidorobjetos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import static javax.persistence.GenerationType.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="atributo")
@Data
public class Atributo {
	@Id
	@GeneratedValue(strategy = TABLE)
    private int id;
    @Column(name="nombre")
    private String nombre;
    @ManyToOne
	@Cascade(CascadeType.ALL)
    private TipoAtributo tipoAtributo;
    @ManyToOne
	@JoinColumn(name = "clase_id")
    private Clase clase;
}
