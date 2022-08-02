package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.persistidorobjetos.annotations.NotPersistable;
import com.example.persistidorobjetos.annotations.Persistable;
import com.example.persistidorobjetos.examples.Auto;
import com.example.persistidorobjetos.examples.Persona3;
import com.example.persistidorobjetos.examples.PersonaConObjetosComplejos;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.InstanciaService;
import com.example.persistidorobjetos.services.SessionService;

@SpringBootTest
@RunWith(SpringRunner.class) 
public class InstanciaServiceTest {

	@Autowired
	InstanciaService instanciaService;
	@Autowired
    ClaseService claseService;
	@Autowired
	SessionService sessionService;
	
	
//	@Test
//	@Transactional
	public void test() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
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
		
		this.sessionService.saveOrUpdateSession(1l);
		Session session = sessionService.getSession(1l);
		
		Object object = persona;
		PropertyDescriptor[] objDescriptors = PropertyUtils.getPropertyDescriptors(object);
		for(PropertyDescriptor descriptor : objDescriptors){
			Object property = PropertyUtils.getProperty(object, descriptor.getName());
			System.out.println(String.valueOf(property));
		}
		
		Clase clase;
		if(claseService.isClaseStored(persona.getClass())){
			clase = claseService.updateClase(persona.getClass());
		}else{
			clase = claseService.generateClaseObject(persona.getClass());
			clase = claseService.saveClase(persona.getClass());
		}
		Instancia instancia = instanciaService.generateInstancia(clase, object, session);
		
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "dni")
				&& Objects.equals(atributoInstancia.getValorAtributo().getValor(), "34334355"))
		);
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "nombre")
				&& Objects.equals(atributoInstancia.getValorAtributo().getValor(), "Juan Carlos"))
		);
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "auto")
				&& atributoInstancia.getValorAtributo().getInstancia() != null)
		);
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "telefonos")
				&& atributoInstancia.getValorAtributo().getValorAtributoList().size() == 3));
		
		instanciaService.saveInstancia(instancia);
		
	}
	
//	@Test
	public void loadInstancia(){
		Instancia instancia = instanciaService.recoverInstancia(1L, 1l);

		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "dni")
				&& Objects.equals(atributoInstancia.getValorAtributo().getValor(), "34334355"))
		);
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "nombre")
				&& Objects.equals(atributoInstancia.getValorAtributo().getValor(), "Juan Carlos"))
		);
		
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "auto")
				&& atributoInstancia.getValorAtributo().getInstancia() != null)
		);
		assertTrue(instancia.getAtributos().stream().anyMatch(atributoInstancia ->
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "telefonos")
				&& atributoInstancia.getValorAtributo().getValorAtributoList().size() == 3));
	}
	
	@Test
	public void loadObjectTest() throws Exception {
		instanciaService.loadObject(1L, PersonaConObjetosComplejos.class);
	}

//	@Test
	public void sarasa(){
		Class<?> clazz = Persona3.class;
		for(Field field : Persona3.class.getDeclaredFields()){
			if(field.isAnnotationPresent(Persistable.class) ||
					(clazz.isAnnotationPresent(Persistable.class) && !field.isAnnotationPresent(NotPersistable.class))){
				System.out.println(field.getGenericType().getTypeName());
			}
		}
	}
	
}
