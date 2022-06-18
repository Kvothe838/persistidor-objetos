package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Getter;
import lombok.Setter;

@Persistable
public class Auto {
	
	@Getter @Setter
	private String marca;
	@Getter @Setter
	private String modelo;
	
}
