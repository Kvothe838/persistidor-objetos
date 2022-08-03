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

import com.example.persistidorobjetos.examples.Persona4;
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
	public void storeRetornaFalse(){
		boolean actualizado = this.persistentObject.store(1, null);

		assertFalse(actualizado);
	}
}
