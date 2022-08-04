package com.example.persistidorobjetos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.persistidorobjetos.examples.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.annotations.Persistable;
import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.AtributoService;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.InstanciaService;
import com.example.persistidorobjetos.services.SessionService;

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
	@Autowired
	InstanciaService instanciaService;
    
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
//	@Commit
	public void saveClaseComplejaWorks() throws Exception {
		this.persistentObject.store(1,new PersonaConObjetosComplejos());
		Clase clase = claseService.getClaseByNombre(PersonaConObjetosComplejos.class.getName());
		List<Atributo> atributos = clase.getAtributos();

		assertEquals(7, atributos.size());
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
				&& atributo.getClase().getAtributos().size() == 3)
		);
  	}
	
	
	@Test
	@Transactional
	public void storeTest() throws Exception{
		Date momentoAntesDelAcceso = new Date(); 
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
		
		Session session = sessionService.getSession(1);
		assertTrue(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
	}

	@Test
	@Transactional
//	@Commit
	public void updateClaseWorks() throws Exception {
		// Esto no es un test real, hay que hacerlo a mano. Correr este método, luego parar, cambiarle un atributo a Persona1 y
		// volver a correr este método para finalmente chequear si se cambió el atributo correcto.
		this.persistentObject.store(1, new PersonaConObjetosComplejos());
	}
	
	@Test
	@Transactional
	public void loadTest() throws Exception{
		Date momentoAntesDelAcceso = new Date(); 
		this.storeTest();
		
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
		
		PersonaConObjetosComplejos personaObtenida = persistentObject.load(1, PersonaConObjetosComplejos.class);
		Session session = sessionService.getSession(1);
		
		assertEquals(persona, personaObtenida);
		assertTrue(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
	}
	
//	@Test
	public void LoadNoHayInstanciaTest() throws Exception{
		PersonaConObjetosComplejos personaObtenida = persistentObject.load(-1, PersonaConObjetosComplejos.class);
		assertEquals(null, personaObtenida);
		personaObtenida = persistentObject.load(1500, PersonaConObjetosComplejos.class);
		assertEquals(null, personaObtenida);
	}
	
	@Test
	@Transactional
	public void storeMasComplicadoTest() throws Exception{
		Date momentoAntesDelAcceso = new Date(); 
		
		PersonaConObjetosComplejos persona = getNewOtraPersonaConObjetosComplejos();
		
		assertTrue(persistentObject.store(1, persona));
		
		Session session = sessionService.getSession(1);
		assertTrue(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
		
		long millis = persistentObject.elapsedTime(1l);
		assertTrue(millis > 0);
		assertTrue(millis < 5000);
	}
	
	@Test
	@Transactional
	public void loadMasComplicadoTest() throws Exception{
		Date momentoAntesDelAcceso = new Date(); 
		PersonaConObjetosComplejos persona = getNewPersonaConObjetosComplejos();
		assertTrue(persistentObject.store(1l, persona));
		
		PersonaConObjetosComplejos personaObtenida = persistentObject.load(1l, PersonaConObjetosComplejos.class);
		Session session = sessionService.getSession(1);
		
		assertEquals(persona, personaObtenida);
		assertTrue(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
		
		long millis = persistentObject.elapsedTime(1l);
		assertTrue(millis > 0);
		assertTrue(millis < 5000);
	}
	
	@Test
	@Transactional
	public void storeUpdateTest() throws Exception{
		Date momentoAntesDelAcceso = new Date(); 
		
		PersonaConObjetosComplejos persona = getNewPersonaConObjetosComplejos();
		PersonaConObjetosComplejos otraPersona = getNewOtraPersonaConObjetosComplejos();
		
		assertTrue(persistentObject.store(1, persona));
		assertTrue(persistentObject.store(1, otraPersona));
		
		PersonaConObjetosComplejos otraPersonaGuardada = persistentObject.load(1l, PersonaConObjetosComplejos.class);
		
		assertEquals(otraPersona, otraPersonaGuardada);
	}
	
	@Test
	@Transactional
	public void existsInstanciaForSession() throws Exception{
		this.storeMasComplicadoTest();
		Class<?> clazz = PersonaConObjetosComplejos.class;
		boolean exists = instanciaService.existsInstanciaByClaseAndSession(1l, clazz.getName());
		assertTrue(exists);
	}
	
	@Test
	@Transactional
	public void noInstanciaForSession() throws Exception{
		this.storeMasComplicadoTest();
		//no hay instancia para esa clase
		Class<?> clazz = Persona3.class;
		boolean exists = instanciaService.existsInstanciaByClaseAndSession(1l, clazz.getName());
		assertFalse(exists);
		
		//no hay instancia para esa session
		clazz = PersonaConObjetosComplejos.class;
		exists = instanciaService.existsInstanciaByClaseAndSession(-1l, clazz.getName());
		assertFalse(exists);
		clazz = Direccion.class;
		exists = instanciaService.existsInstanciaByClaseAndSession(1l, clazz.getName());
		assertFalse(exists);
	}
	
	@Test
	@Transactional
	public void deleteInstanciaForSessionTest() throws Exception{
		PersonaConObjetosComplejos persona = getNewPersonaConObjetosComplejos();
		assertTrue(persistentObject.store(1l, persona));
		
		Date momentoAntesDelAcceso = new Date(); 
		PersonaConObjetosComplejos personaBorrada = persistentObject.delete(1l, PersonaConObjetosComplejos.class);
		
		assertEquals(persona, personaBorrada);
		assertFalse(persistentObject.exists(1l, PersonaConObjetosComplejos.class));
		
		Session session = sessionService.getSession(1);
		assertTrue(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
		
		long millis = persistentObject.elapsedTime(1l);
		assertTrue(millis > 0);
		assertTrue(millis < 5000);
	}
	
	@Test
	@Transactional
	public void noInstanciaToDeleteTest() throws Exception{
		Date momentoAntesDelAcceso = new Date(); 
		Direccion direccion = persistentObject.delete(1l, Direccion.class);
		
		assertEquals(null, direccion);
		assertFalse(persistentObject.exists(1l, PersonaConObjetosComplejos.class));
		
		Session session = sessionService.getSession(1l);
		assertTrue(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
		
		Long millis = persistentObject.elapsedTime(1l);
		assertTrue(millis > 0);
		assertTrue(millis < 5000);
	}
	
	@Test
	@Transactional
	public void noSessionToDeleteInstanciaTest() throws Exception{
		sessionService.saveOrUpdateSession(1l);
		Direccion direccion = persistentObject.delete(-1l, Direccion.class);
		
		assertEquals(null, direccion);
		assertFalse(persistentObject.exists(1l, PersonaConObjetosComplejos.class));
		
		Date momentoAntesDelAcceso = new Date(); 
		Session session = sessionService.getSession(1l);
		assertFalse(session.getUltimoAcceso().compareTo(momentoAntesDelAcceso) > 0);
	}
	
	
	
	private PersonaConObjetosComplejos getNewPersonaConObjetosComplejos(){
		PersonaConObjetosComplejos persona = new PersonaConObjetosComplejos();
		persona.setDni(34334355);
		persona.setNombre("Juan Carlos");
		ArrayList<String> telefonos = new ArrayList<String>();
		telefonos.add("15-4585-5454");
		telefonos.add("15-1221-1221");
		telefonos.add("15-6655-6655");
		persona.setTelefonos(telefonos);
		//no instancia kilometros recorridos
		Auto auto = new Auto();
		auto.setMarca("Fiat");
		auto.setModelo("600");
		persona.setAuto(auto);
		Direccion direccion1 = new Direccion();
		direccion1.setCalle("Calle Falsa");
		direccion1.setCodigoPostal("1234");
		direccion1.setLocalidad("Mad Remia");
		direccion1.setNumero(123);
		direccion1.setPais("Argentina");
		direccion1.setProvincia("Buenos Aires");
		Direccion direccion2 = new Direccion();
		direccion2.setCalle("Calle Falsa Paralela");
		direccion2.setCodigoPostal("4567");
		direccion2.setLocalidad("Mad Remia");
		direccion2.setNumero(123);
		direccion2.setPais("Argentina");
		direccion2.setProvincia("Chubut");
		Direccion direccion3 = null;
		//una de las direcciones es null
		ArrayList<Direccion> direcciones = new ArrayList<>(Arrays.asList(direccion1, direccion2, direccion3));
		persona.setDirecciones(direcciones);
		//uno de los Integer es null
		ArrayList<Integer> numerosFavoritos = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,null));
		persona.setNumerosFavoritos(numerosFavoritos);
		persona.setLeGustaElArte(true);
		return persona;
	}
	
	private PersonaConObjetosComplejos getNewOtraPersonaConObjetosComplejos(){
		PersonaConObjetosComplejos persona = new PersonaConObjetosComplejos();
		persona.setDni(42566899);
		persona.setNombre("Maria Belen");
		ArrayList<String> telefonos = new ArrayList<String>();
		telefonos.add("15-7777-5454");
		telefonos.add("15-3366-1221");
		persona.setTelefonos(telefonos);
		//no instancia kilometros recorridos
		Auto auto = new Auto();
		auto.setMarca("Chevrolet");
		auto.setModelo("Agile");
		persona.setAuto(auto);
		Direccion direccion1 = new Direccion();
		direccion1.setCalle("Calle Falsa");
		direccion1.setCodigoPostal("1234");
		direccion1.setLocalidad("Mad Remia");
		direccion1.setNumero(123);
		direccion1.setPais("Argentina");
		direccion1.setProvincia("Buenos Aires");
		Direccion direccion2 = new Direccion();
		direccion2.setCalle("Calle Falsa Paralela");
		direccion2.setCodigoPostal("4567");
		direccion2.setLocalidad("Mad Remia");
		direccion2.setNumero(123);
		direccion2.setPais("Argentina");
		direccion2.setProvincia("Chubut");
		Direccion direccion3 = null;
		//una de las direcciones es null
		ArrayList<Direccion> direcciones = new ArrayList<>(Arrays.asList(direccion1, direccion2, direccion3));
		persona.setDirecciones(direcciones);
		//uno de los Integer es null
		ArrayList<Integer> numerosFavoritos = new ArrayList<>(Arrays.asList(11,12,13,14,15,16,17,null));
		persona.setNumerosFavoritos(numerosFavoritos);
		persona.setLeGustaElArte(false);
		return persona;
	}

	@Test
	@Transactional
	public void existsFunciona() {
		Persona1 persona = new Persona1();
		persona.setDni(12345);
		persona.setNombre("Pedro");

		assertFalse(this.persistentObject.exists(1, Persona1.class));

		assertDoesNotThrow(() -> this.persistentObject.store(1, persona));

		assertTrue(this.persistentObject.exists(1, Persona1.class));
		assertFalse(this.persistentObject.exists(2, Persona1.class));
		assertFalse(this.persistentObject.exists(1, Persona2.class));
	}
	
}
