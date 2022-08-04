package com.example.persistidorobjetos.model;

import static javax.persistence.GenerationType.TABLE;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="clase")
public class Clase {
	
    @Id
	@GeneratedValue(strategy = TABLE)
    private Long id;
    @Column(name="nombre")
    private String nombre;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Atributo> atributos;
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clase other = (Clase) obj;
		if (atributos == null) {
			if (other.atributos != null)
				return false;
		} else {
			if(atributos.size() != other.getAtributos().size())
				return false;
			for(Atributo atributo : atributos){
				if(!other.getAtributos().contains(atributo))
					return false;
			}
		}
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atributos == null) ? 0 : atributos.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
	
	
	
	
	
	
}
