package com.example.persistidorobjetos.examples;

import java.util.ArrayList;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Data;

@Data
@Persistable
public class PersonaConObjetosComplejos {
	private int dni;
	private String nombre;
	private Auto auto;
	private ArrayList<String> telefonos;
}
