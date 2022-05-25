package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.persistidorobjetos.examples.Persona1;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.services.ClaseService;

@SpringBootTest
@RunWith(SpringRunner.class) 
public class PersistentObjectTest {
	
	@Autowired
	PersistentObject persistentObject;
    @Autowired
    ClaseService claseService;

    @Test
    public void SaveWorksTest(){
        persistentObject.store(1, new Persona1());

        Clase clase = this.claseService.getClaseByNombre("Persona1");
        assertNotNull(clase);
    }
}
