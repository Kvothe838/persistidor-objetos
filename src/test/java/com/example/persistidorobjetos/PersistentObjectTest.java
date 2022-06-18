package com.example.persistidorobjetos;

import java.lang.reflect.Field;
import java.util.*;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.model.TipoAtributo;
import com.example.persistidorobjetos.services.AtributoService;
import com.example.persistidorobjetos.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.persistidorobjetos.annotations.Persistable;
import com.example.persistidorobjetos.examples.Persona1;
import com.example.persistidorobjetos.examples.Persona4;
import com.example.persistidorobjetos.examples.Persona5;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.services.ClaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

import static org.junit.jupiter.api.Assertions.*;

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

	@Before
	@Transactional
	public void cleanDatabase(){
		CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
		CriteriaDelete<Clase> criteriaDeleteClase = criteriaBuilder.createCriteriaDelete(Clase.class);
		criteriaDeleteClase.from(Clase.class);
		int rowsDeletedClase = this.em.createQuery(criteriaDeleteClase).executeUpdate();
		System.out.println("Clase entities deleted: " + rowsDeletedClase);

		CriteriaDelete<Atributo> criteriaDeleteAtributo = criteriaBuilder.createCriteriaDelete(Atributo.class);
		criteriaDeleteAtributo.from(Atributo.class);
		int rowsDeletedAtributo = this.em.createQuery(criteriaDeleteAtributo).executeUpdate();
		System.out.println("Atributo entities deleted: " + rowsDeletedAtributo);

		CriteriaDelete<TipoAtributo> criteriaDeleteTipoAtributo = criteriaBuilder.createCriteriaDelete(TipoAtributo.class);
		criteriaDeleteTipoAtributo.from(TipoAtributo.class);
		int rowsDeletedTipoAtributo = this.em.createQuery(criteriaDeleteTipoAtributo).executeUpdate();
		System.out.println("TipoAtributo entities deleted: " + rowsDeletedTipoAtributo);

		CriteriaDelete<Session> criteriaDeleteSession = criteriaBuilder.createCriteriaDelete(Session.class);
		criteriaDeleteSession.from(Session.class);
		int rowsDeletedSession = this.em.createQuery(criteriaDeleteSession).executeUpdate();
		System.out.println("Session entities deleted: " + rowsDeletedSession);
	}
    
//    @Test
	public void AnnotationPresent() throws NoSuchFieldException, SecurityException{
		Persona1 persona1 = new Persona1();
		Persona4 persona4 = new Persona4();
		Persona5 persona5 = new Persona5();
		Class<?> clazz;

		clazz = persona1.getClass();
		assertTrue(clazz.isAnnotationPresent(Persistable.class));

		clazz = persona4.getClass();
		assertFalse(clazz.isAnnotationPresent(Persistable.class));
		Field[] fields = clazz.getDeclaredFields();
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
	public void elapsedTimeWorks(){
		Date now = new Date();
		long sId = 1;

		this.sessionService.saveSession(sId, now);

		long elapsedTime = this.persistentObject.elapsedTime(sId);
		System.out.printf("elapsed time: %d%n", elapsedTime);

		assertNotEquals(-1, elapsedTime, "elapsedTime no debe ser -1 para session persistida");

		// No se puede chequear igualdad ya que elapsedTime calcula el tiempo desde el último acceso hasta ahora (new Date()).
		// Y no tenemos acceso a esa instancia de Date dentro de elapsedTime().
		// Sin embargo, como es el tiempo en milisegundos y a nivel código no pasa casi nada desde la
		// instanciación de now en este método hasta la comparación en elaspedTime(), se puede testear
		// que sea menor a 1000 milisegundos ya que 1 segundo es muchísimo para esta diferencia de ejecuciones.

		assertTrue(elapsedTime < 1000, "elapsedTime debe ser menor a un segundo");

	}
}
