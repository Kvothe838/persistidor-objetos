package com.example.persistidorobjetos.examples;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Persona3
{
	@Getter @Setter
	private int dni;
	@Getter @Setter
	private String nombre;
	@Getter @Setter
	private ArrayList<String> telefonos;
	@Getter @Setter
	private ArrayList<Direccion> direcciones;
}
