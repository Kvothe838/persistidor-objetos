package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Data;

@Persistable
@Data
public class Persona1
{
	private int dni;
	private String nombre;
}
