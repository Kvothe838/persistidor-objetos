package com.example.persistidorobjetos.examples;

import java.util.List;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Getter;
import lombok.Setter;

@Persistable
public class PersonaConObjetosComplejos {

	@Getter	@Setter
	private int dni;
	@Getter @Setter
	private String nombre;
	@Getter @Setter
	private Auto auto;
	@Getter @Setter
	private List<String> tarjetasDeCredito; 
	
}
