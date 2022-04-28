package com.example.persistidorobjetos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersistidorObjetosApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PersistidorObjetosApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Hello world");
	}
}
