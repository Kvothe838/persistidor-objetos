package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

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

@SpringBootTest
@RunWith(SpringRunner.class) 
public class PersistentObjectTest {
	
	@Autowired
	PersistentObject persistentObject;
    @Autowired
    ClaseService claseService;

//    @Test
    public void SaveClaseWorks(){
        persistentObject.store(1, new Persona1());

        Clase clase = this.claseService.getClaseByNombre("Persona1");
        assertNotNull(clase);
        assertEquals(clase.getNombre(),"Persona1");
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
  public void generateClaseWorks(){
      Clase clase = this.claseService.generateClaseObject(Persona1.class);
      assertNotNull(clase);
  }
}
