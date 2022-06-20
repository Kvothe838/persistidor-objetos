package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.persistidorobjetos.examples.Persona2;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.InstanciaService;

@SpringBootTest
@RunWith(SpringRunner.class) 
public class InstanciaServiceTest {

	@Autowired
	InstanciaService instanciaService;
	@Autowired
    ClaseService claseService;
	
	
	@Test
	public void test() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
		Persona2 persona2 = new Persona2();
		persona2.setDni(34334355);
		persona2.setNombre("Juan Carlos");
		ArrayList<String> telefonos = new ArrayList<String>();
		telefonos.add("15-4585-5454");
		telefonos.add("15-1221-1221");
		telefonos.add("15-6655-6655");
		persona2.setTelefonos(telefonos);
		
		Object object = persona2;
		PropertyDescriptor[] objDescriptors = PropertyUtils.getPropertyDescriptors(object);
		for(PropertyDescriptor descriptor : objDescriptors){
			Object property = PropertyUtils.getProperty(object, descriptor.getName());
			System.out.println(String.valueOf(property));
		}
		
		Clase clase = claseService.generateClaseObject(persona2.getClass());
		Session session = new Session();
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
				Objects.equals(atributoInstancia.getAtributo().getNombre(), "telefonos"))
		);
		
	}
	
}
