package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Data;

@Persistable
@Data
public class Direccion
{
	private String calle;
	private int numero;
	private String codigoPostal;
	private String localidad;
	private String provincia;
	private String pais;
}
