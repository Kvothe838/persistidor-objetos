package com.example.persistidorobjetos;

import com.example.persistidorobjetos.examples.Persona1;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.services.ClaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersistentObjectTest {
    @Autowired
    ClaseService claseService;

    @Test
    public void SaveWorks(){
        PersistentObject persistentObject = new PersistentObject();
        persistentObject.store(1, new Persona1());

        Clase clase = this.claseService.getClaseByNombre("Persona1");
        assertNotNull(clase);
    }
}
