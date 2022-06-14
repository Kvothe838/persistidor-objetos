package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

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
