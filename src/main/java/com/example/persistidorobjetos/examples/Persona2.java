package com.example.persistidorobjetos.examples;

import java.util.ArrayList;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Data;

@Persistable
@Data
public class Persona2
{
	private int dni;
	private String nombre;
	private ArrayList<String> telefonos;
}
