package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.examples.Persona1;

import java.util.List;
import java.util.Objects;

@SpringBootTest
@RunWith(SpringRunner.class) 
public class PersistentObjectTest {
	@Autowired
	EntityManager em;
	@Autowired
	PersistentObject persistentObject;
	@Autowired
	ClaseService claseService;
	@Autowired
	SessionService sessionService;

	@Before
	@Transactional
	public void cleanDatabase(){
		Query queryAtributo = this.em.createNativeQuery("DELETE FROM atributo");
		Query queryTipoAtributo = this.em.createNativeQuery("DELETE FROM tipo_atributo");
		Query queryClase = this.em.createNativeQuery("DELETE FROM clase");
		Query querySession = this.em.createNativeQuery("DELETE FROM session");

		queryAtributo.executeUpdate();
		queryTipoAtributo.executeUpdate();
		queryClase.executeUpdate();
		querySession.executeUpdate();
	}

	@Test
	@Transactional
	public void storeRetornaFalse(){
		boolean actualizado = this.persistentObject.store(1, new Persona1());

		assertFalse(actualizado);
	}

	@Test
	@Transactional
	public void storeGuardaPersona1(){
		Persona1 persona = new Persona1();
		String nombre = "Juan";
		int dni = 40302789;
		long sId = 1;

		persona.setNombre(nombre);
		persona.setDni(dni);

		assertDoesNotThrow(() -> this.persistentObject.store(sId, persona));

		Session session = this.sessionService.getOrSave(sId);

		assertNotNull(session);

		String nombreClase = Persona1.class.getName();

		Clase clase = this.claseService.get(nombreClase, session.getId());

		assertNotNull(clase);

		assertEquals(clase.getNombre(), nombreClase);

		List<Atributo> atributos = clase.getAtributos();

		assertNotNull(atributos);
		assertEquals(2, atributos.size());
		assertTrue(atributos.stream().anyMatch(atributo ->
				Objects.equals(atributo.getNombre(), "nombre")
				&& atributo.getTipoAtributo() != null
				&& Objects.equals(atributo.getTipoAtributo().getNombre(), "java.lang.String")));
		assertTrue(atributos.stream().anyMatch(atributo ->
				Objects.equals(atributo.getNombre(), "dni")
						&& atributo.getTipoAtributo() != null
						&& Objects.equals(atributo.getTipoAtributo().getNombre(), "int")));
	}
}
