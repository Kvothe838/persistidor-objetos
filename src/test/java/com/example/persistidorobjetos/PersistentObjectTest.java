package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

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
import com.example.persistidorobjetos.model.TipoAtributo;
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

//	@Before
	@Transactional
	public void cleanDatabase(){
		String hql = "DELETE FROM clase_atributos";
	    Query q = this.em.createNativeQuery(hql);
	    q.executeUpdate();
	        
	    CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
		
	    CriteriaDelete<Atributo> criteriaDeleteAtributo = criteriaBuilder.createCriteriaDelete(Atributo.class);
		criteriaDeleteAtributo.from(Atributo.class);
		int rowsDeletedAtributo = this.em.createQuery(criteriaDeleteAtributo).executeUpdate();
		System.out.println("Atributo entities deleted: " + rowsDeletedAtributo);
		
		CriteriaDelete<Clase> criteriaDeleteClase = criteriaBuilder.createCriteriaDelete(Clase.class);
		criteriaDeleteClase.from(Clase.class);
		int rowsDeletedClase = this.em.createQuery(criteriaDeleteClase).executeUpdate();
		System.out.println("Clase entities deleted: " + rowsDeletedClase);

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

//	@Test
	@Transactional
	public void generateClaseWorks(){
	  Clase clase = this.claseService.generateClaseObject(Persona1.class);
	  assertNotNull(clase);
	}

//	@Test
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

//	@Test
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
	
	
	

}
