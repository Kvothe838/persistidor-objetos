package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Getter;
import lombok.Setter;

@Persistable
public class Persona1
{
	@Getter	@Setter
	private int dni;
	@Getter @Setter
	private String nombre;
}
