package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.annotations.Persistable;
import com.example.persistidorobjetos.examples.Auto;
import com.example.persistidorobjetos.examples.Persona1;
import com.example.persistidorobjetos.examples.Persona4;
import com.example.persistidorobjetos.examples.Persona5;
import com.example.persistidorobjetos.examples.PersonaConObjetosComplejos;
import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.AtributoService;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.InstanciaService;
import com.example.persistidorobjetos.services.SessionService;

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
	AtributoService atributoService;
	@Autowired
	SessionService sessionService;
	@Autowired
	InstanciaService instanciaService;

	@Before
	@Transactional
	@Commit
	public void cleanDatabase(){
		Query queryAtributoInstancia = this.em.createNativeQuery("DELETE FROM atributo_instancia");
		Query queryValorAtributo = this.em.createNativeQuery("DELETE FROM valor_atributo");
		Query queryInstancia = this.em.createNativeQuery("DELETE FROM instancia");
		Query queryAtributo = this.em.createNativeQuery("DELETE FROM atributo");
		Query queryTipoAtributo = this.em.createNativeQuery("DELETE FROM tipo_atributo");
		Query queryClase = this.em.createNativeQuery("DELETE FROM clase");
		Query querySession = this.em.createNativeQuery("DELETE FROM session");

		int rowsDeleted = queryAtributoInstancia.executeUpdate();
		System.out.println("atributo_instancia entities deleted: " + rowsDeleted);
		rowsDeleted = queryValorAtributo.executeUpdate();
		System.out.println("valor_atributo entities deleted: " + rowsDeleted);
		rowsDeleted = queryInstancia.executeUpdate();
		System.out.println("instancia entities deleted: " + rowsDeleted);
		rowsDeleted = queryAtributo.executeUpdate();
		System.out.println("atributo entities deleted: " + rowsDeleted);
		rowsDeleted = queryTipoAtributo.executeUpdate();
		System.out.println("tipo_atributo entities deleted: " + rowsDeleted);
		rowsDeleted = queryClase.executeUpdate();
		System.out.println("clase entities deleted: " + rowsDeleted);
		rowsDeleted = querySession.executeUpdate();
		System.out.println("session entities deleted: " + rowsDeleted);
	}
    
	@Test
	@Transactional
	public void AnnotationPresent() throws NoSuchFieldException, SecurityException{
		Persona1 persona1 = new Persona1();
		Persona4 persona4 = new Persona4();
		Persona5 persona5 = new Persona5();
		Class<?> clazz;

		clazz = persona1.getClass();
		assertTrue(clazz.isAnnotationPresent(Persistable.class));

		clazz = persona4.getClass();
		assertFalse(clazz.isAnnotationPresent(Persistable.class));
		Field field = clazz.getDeclaredField("dni");
		assertTrue(field.isAnnotationPresent(Persistable.class));
		field = clazz.getDeclaredField("nombre");
		assertFalse(field.isAnnotationPresent(Persistable.class));

		clazz = persona5.getClass();
		assertFalse(clazz.isAnnotationPresent(Persistable.class));
		for(Field f : clazz.getDeclaredFields()){
			assertFalse(f.isAnnotationPresent(Persistable.class));
		}
	}

	@Test
	@Transactional
	public void generateClaseWorks(){
	  Clase clase = this.claseService.generateClaseObject(Persona1.class);
	  assertNotNull(clase);
	}

	@Test
	@Transactional
	public void saveClaseWorks() throws Exception {
		this.persistentObject.store(1, new Persona1());

		List<Atributo> atributos = this.atributoService.getAll();

		assertEquals(2, atributos.size());
		assertTrue(atributos.stream().anyMatch(atributo ->
			Objects.equals(atributo.getNombre(), "dni")
				&& Objects.equals(atributo.getTipoAtributo().getNombre(), int.class.getName())
			)
		);
		assertTrue(atributos.stream().anyMatch(atributo ->
			Objects.equals(atributo.getNombre(), "nombre")
					&& Objects.equals(atributo.getTipoAtributo().getNombre(), String.class.getName())
			)
		);
  	}

	@Test
	@Transactional
	public void saveSessionWorks() throws Exception {
		this.persistentObject.store(1, new Persona1());

		Session session = this.sessionService.getSession(1);

		assertNotNull(session);
		assertEquals(1, session.getId());
		assertNotNull(session.getUltimoAcceso());
	}
	
	@Test
	@Transactional
	@Commit
	public void saveClaseComplejaWorks() throws Exception {
		this.persistentObject.store(1,new PersonaConObjetosComplejos());
		Clase clase = claseService.getClaseByNombre(PersonaConObjetosComplejos.class.getName());
		List<Atributo> atributos = clase.getAtributos();

		assertEquals(4, atributos.size());
		assertTrue(atributos.stream().anyMatch(atributo ->
			Objects.equals(atributo.getNombre(), "dni")
				&& Objects.equals(atributo.getTipoAtributo().getNombre(), int.class.getName())
			)
		);
		assertTrue(atributos.stream().anyMatch(atributo ->
			Objects.equals(atributo.getNombre(), "nombre")
					&& Objects.equals(atributo.getTipoAtributo().getNombre(), String.class.getName())
			)
		);
		assertTrue(atributos.stream().anyMatch(atributo ->
		Objects.equals(atributo.getNombre(), "auto")
				&& Objects.equals(atributo.getTipoAtributo().getNombre(), Auto.class.getName())
				&& atributo.getClase().getAtributos().size() == 2)
		);
  	}
	
	@Test
	@Transactional
	public void storeTest() throws Exception{
		PersonaConObjetosComplejos persona = new PersonaConObjetosComplejos();
		persona.setDni(34334355);
		persona.setNombre("Juan Carlos");
		ArrayList<String> telefonos = new ArrayList<String>();
		telefonos.add("15-4585-5454");
		telefonos.add("15-1221-1221");
		telefonos.add("15-6655-6655");
		persona.setTelefonos(telefonos);
		Auto auto = new Auto();
		auto.setMarca("Fiat");
		auto.setModelo("600");
		persona.setAuto(auto);
		
		assertTrue(persistentObject.store(1, persona));
	}

	@Test
	@Transactional
	@Commit
	public void updateClaseWorks() throws Exception {
		// Esto no es un test real, hay que hacerlo a mano. Correr este método, luego parar, cambiarle un atributo a Persona1 y
		// volver a correr este método para finalmente chequear si se cambió el atributo correcto.
		this.persistentObject.store(1, new PersonaConObjetosComplejos());
	}
}
