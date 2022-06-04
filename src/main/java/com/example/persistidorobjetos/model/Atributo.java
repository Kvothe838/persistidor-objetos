package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="atributo")
public class Atributo {
	
    @Id
    private int id;
    @Column(name="nombre")
    private String nombre;
    @ManyToOne
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private TipoAtributo tipoAtributo;
    @ManyToOne
    private Clase clase;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public TipoAtributo getTipoAtributo() {
		return tipoAtributo;
	}
	public void setTipoAtributo(TipoAtributo tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}
	public Clase getClase() {
		return clase;
	}
	public void setClase(Clase clase) {
		this.clase = clase;
	}
	
}
