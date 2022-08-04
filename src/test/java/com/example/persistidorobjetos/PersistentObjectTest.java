package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.persistidorobjetos.examples.Persona2;
import com.example.persistidorobjetos.model.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

		List<Instancia> instancias = clase.getInstancias();

		assertNotNull(instancias);
		assertEquals(1, instancias.size());

		Instancia instanciaPrincipal = clase.getInstanciaPrincipal();

		assertNotNull(instanciaPrincipal);

		List<ValorAtributo> valores = instanciaPrincipal.getAtributos();

		assertEquals(2, valores.size());

		assertTrue(valores.stream().anyMatch(valorAtributo ->
				valorAtributo.getInstancia() == null
				&& valorAtributo.getAtributo() != null
				&& Objects.equals(valorAtributo.getAtributo().getNombre(), "nombre")
				&& valorAtributo.getValores() != null
				&& valorAtributo.getValores().size() == 1
				&& valorAtributo.getValores().contains(nombre)));

		assertTrue(valores.stream().anyMatch(valorAtributo ->
				valorAtributo.getInstancia() == null
						&& valorAtributo.getAtributo() != null
						&& Objects.equals(valorAtributo.getAtributo().getNombre(), "dni")
						&& valorAtributo.getValores() != null
						&& valorAtributo.getValores().size() == 1
						&& valorAtributo.getValores().contains(String.valueOf(dni))));
	}

	@Test
	@Transactional
	public void storeGuardaPersona2(){
		Persona2 persona = new Persona2();
		String nombre = "Juan";
		int dni = 40302789;
		String telefono1 = "6070809";
		String telefono2 = "21791035198751";
		ArrayList<String> telefonos = new ArrayList<>(Arrays.asList(telefono1, telefono2));
		long sId = 1;

		persona.setNombre(nombre);
		persona.setDni(dni);
		persona.setTelefonos(telefonos);

		assertDoesNotThrow(() -> this.persistentObject.store(sId, persona));

		Session session = this.sessionService.getOrSave(sId);

		assertNotNull(session);

		String nombreClase = Persona2.class.getName();

		Clase clase = this.claseService.get(nombreClase, session.getId());

		assertNotNull(clase);

		assertEquals(clase.getNombre(), nombreClase);

		List<Atributo> atributos = clase.getAtributos();

		assertNotNull(atributos);
		assertEquals(3, atributos.size());
		assertTrue(atributos.stream().anyMatch(atributo ->
				Objects.equals(atributo.getNombre(), "nombre")
						&& atributo.getTipoAtributo() != null
						&& Objects.equals(atributo.getTipoAtributo().getNombre(), "java.lang.String")));
		assertTrue(atributos.stream().anyMatch(atributo ->
				Objects.equals(atributo.getNombre(), "dni")
						&& atributo.getTipoAtributo() != null
						&& Objects.equals(atributo.getTipoAtributo().getNombre(), "int")));
		assertTrue(atributos.stream().anyMatch(atributo ->
				Objects.equals(atributo.getNombre(), "telefonos")
						&& atributo.getTipoAtributo() != null
						&& Objects.equals(atributo.getTipoAtributo().getNombre(), "java.util.ArrayList")));

		List<Instancia> instancias = clase.getInstancias();

		assertNotNull(instancias);
		assertEquals(1, instancias.size());

		Instancia instanciaPrincipal = clase.getInstanciaPrincipal();

		assertNotNull(instanciaPrincipal);

		List<ValorAtributo> valores = instanciaPrincipal.getAtributos();

		assertNotNull(valores);
		assertEquals(3, valores.size());

		assertTrue(valores.stream().anyMatch(valorAtributo ->
				valorAtributo.getInstancia() == null
						&& valorAtributo.getAtributo() != null
						&& Objects.equals(valorAtributo.getAtributo().getNombre(), "nombre")
						&& valorAtributo.getValores() != null
						&& valorAtributo.getValores().size() == 1
						&& valorAtributo.getValores().contains(nombre)));

		assertTrue(valores.stream().anyMatch(valorAtributo ->
				valorAtributo.getInstancia() == null
						&& valorAtributo.getAtributo() != null
						&& Objects.equals(valorAtributo.getAtributo().getNombre(), "dni")
						&& valorAtributo.getValores() != null
						&& valorAtributo.getValores().size() == 1
						&& valorAtributo.getValores().contains(String.valueOf(dni))));
		System.out.println(valores);
		assertTrue(valores.stream().anyMatch(valorAtributo ->
				valorAtributo.getInstancia() == null
						&& valorAtributo.getAtributo() != null
						&& Objects.equals(valorAtributo.getAtributo().getNombre(), "telefonos")
						&& valorAtributo.getValores() != null
						&& valorAtributo.getValores().size() == 2
						&& valorAtributo.getValores().contains(telefono1)
						&& valorAtributo.getValores().contains(telefono2)));
	}
}
