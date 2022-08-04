package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.hsqldb.DatabaseManager.getSession;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;

    public Clase get(String nombre, long sessionId){
        String queryStr = "select c from Clase c where c.nombre = :nombre AND c.session.id = :sessionId";
        Query query = this.em.createQuery(queryStr)
                .setParameter("nombre", nombre)
                .setParameter("sessionId", sessionId);

        return (Clase) query.getSingleResult();
    }
}
