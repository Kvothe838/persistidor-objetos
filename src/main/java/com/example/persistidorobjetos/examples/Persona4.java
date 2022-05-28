package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Getter;
import lombok.Setter;

public class Persona4 {

	@Getter	@Setter @Persistable
	private int dni;
	@Getter @Setter
	private String nombre;
	
}
