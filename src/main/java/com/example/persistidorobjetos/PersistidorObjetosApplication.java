package com.example.persistidorobjetos;

import com.example.persistidorobjetos.examples.Persona1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersistidorObjetosApplication implements CommandLineRunner {
	@Autowired
	private PersistentObject persistentObject;
	public static void main(String[] args) {
		SpringApplication.run(PersistidorObjetosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Ejemplo de uso.
		/*Persona1 persona = new Persona1();
		persona.setNombre("Juan");
		persona.setDni(1234);
		boolean guardado = persistentObject.store(1, persona);
		System.out.printf("Objeto guardado: %s\n", guardado);
		Persona1 personaCargada = persistentObject.load(1, Persona1.class);
		System.out.printf("Persona cargada. Nombre: %s | dni: %d\n", personaCargada.getNombre(), personaCargada.getDni());
		boolean exists = persistentObject.exists(1, Persona1.class);
		System.out.printf("Existe: %s\n", exists);
		long tiempo = persistentObject.elapsedTime(1);
		System.out.printf("Tiempo que pasó desde último acceso: %d\n", tiempo);
		persistentObject.delete(1, Persona1.class);
		System.out.println("Persona borrada");
		exists = persistentObject.exists(1, Persona1.class);
		System.out.printf("Existe: %s\n", exists);*/
	}
}
