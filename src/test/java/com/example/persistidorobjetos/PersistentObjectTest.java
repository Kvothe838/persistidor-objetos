package com.example.persistidorobjetos;

import static org.junit.jupiter.api.Assertions.*;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.examples.Persona1;

@SpringBootTest
@RunWith(SpringRunner.class) 
public class PersistentObjectTest {
	@Autowired
	EntityManager em;
	@Autowired
	PersistentObject persistentObject;

	@Before
	@Transactional
	public void cleanDatabase(){
		/*Query queryAtributoInstancia = this.em.createNativeQuery("DELETE FROM atributo_instancia");
		queryAtributoInstancia.executeUpdate();*/
	}

	@Test
	@Transactional
	public void storeRetornaFalse(){
		boolean actualizado = this.persistentObject.store(1, new Persona1());

		assertFalse(actualizado);
	}
}
