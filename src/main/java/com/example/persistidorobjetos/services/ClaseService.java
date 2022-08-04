package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import static org.hsqldb.DatabaseManager.getSession;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;

    public Clase get(String nombre, int sessionId){
        try{
            String queryStr = "select c from Clase c where c.nombre = :nombre AND c.session.id = :sessionId";
            Query query = this.em.createQuery(queryStr)
                    .setParameter("nombre", nombre)
                    .setParameter("sessionId", sessionId);

            return (Clase) query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
    }

    public void save(String nombre, Session session) {
        Clase clase = new Clase();

        clase.setSession(session);
        clase.setNombre(nombre);

        this.em.persist(clase);
    }

    public Clase getOrSave(String clazzName, Session session) {
        Clase clase = this.get(clazzName, session.getId());

        if(clase == null){
            this.save(clazzName, session);
            clase = this.get(clazzName, session.getId());
        }

        return clase;
    }

    public void borrarAtributos(Clase clase) {
        clase.setAtributos(null);
    }
}
