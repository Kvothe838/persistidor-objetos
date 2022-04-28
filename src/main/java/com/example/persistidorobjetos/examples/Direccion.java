package com.example.persistidorobjetos.examples;

import lombok.Getter;
import lombok.Setter;

public class Direccion
{
	@Getter @Setter
	private String calle;
	@Getter @Setter
	private int numero;
	@Getter @Setter
	private String codigoPostal;
	@Getter @Setter
	private String localidad;
	@Getter @Setter
	private String provincia;
	@Getter @Setter
	private String pais;
}
