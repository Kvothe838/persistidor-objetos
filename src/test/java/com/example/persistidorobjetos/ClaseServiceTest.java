package com.example.persistidorobjetos;

import com.example.persistidorobjetos.examples.Persona1;
import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClaseServiceTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private ClaseService claseService;
    @Autowired
    private SessionService sessionService;

    @Before
    @Transactional
    public void cleanDatabase(){
        Query queryAtributo = this.em.createNativeQuery("DELETE FROM atributo");
        Query queryTipoAtributo = this.em.createNativeQuery("DELETE FROM tipo_atributo");
        Query queryClase = this.em.createNativeQuery("DELETE FROM clase");
        Query querySession = this.em.createNativeQuery("DELETE FROM session");

        queryAtributo.executeUpdate();
        queryTipoAtributo.executeUpdate();
        queryClase.executeUpdate();
        querySession.executeUpdate();
    }

    @Test
    @Transactional
    public void getConNombreYSessionId(){
        Session session = new Session();
        Date ultimoAcceso = new Date();
        session.setUltimoAcceso(ultimoAcceso);

        this.em.persist(session);
        this.em.flush();

        String nombreClase = Persona1.class.getName();
        Clase clase = new Clase();

        clase.setNombre(nombreClase);
        clase.setSession(session);

        this.em.persist(clase);

        Clase claseRecuperada = this.claseService.get(nombreClase, session.getId());

        assertNotNull(claseRecuperada);
        assertNotEquals(0, claseRecuperada.getId());
        assertEquals(claseRecuperada.getNombre(), nombreClase);

        Session sessionRecuperada = claseRecuperada.getSession();

        assertNotNull(sessionRecuperada);
        assertNotEquals(0, sessionRecuperada.getId());
        assertEquals(sessionRecuperada.getUltimoAcceso(), ultimoAcceso);
    }

    @Test
    @Transactional
    public void borrarAtributosTest(){
        long sId = 1;

        Session session = this.sessionService.getOrSave(sId);
        String nombreClase = Persona1.class.getName();
        Clase clase = this.claseService.getOrSave(nombreClase, session);

        Atributo atributo1 = new Atributo();
        atributo1.setNombre("atributo1");
        Atributo atributo2 = new Atributo();
        atributo2.setNombre("atributo2");
        clase.setAtributos(new ArrayList<>(Arrays.asList(atributo1, atributo2)));

        this.em.merge(clase);

        this.claseService.borrarAtributosEInstancias(clase, Persona1.class);

        Clase clasePersistida = this.claseService.getOrSave(nombreClase, session);

        assertNull(clasePersistida.getAtributos());
        assertNull(clasePersistida.getInstancias());
    }
}
