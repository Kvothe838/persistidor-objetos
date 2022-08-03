package com.example.persistidorobjetos.examples;

import com.example.persistidorobjetos.annotations.Persistable;

import lombok.Data;

@Persistable
@Data
public class Auto {
	private String marca;
	private String modelo;
}
