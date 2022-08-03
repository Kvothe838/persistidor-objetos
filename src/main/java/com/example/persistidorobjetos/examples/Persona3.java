package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Persistable
@Data
public class Persona3
{
	private int dni;
	private String nombre;
	private ArrayList<String> telefonos;
	private ArrayList<Direccion> direcciones;
}
