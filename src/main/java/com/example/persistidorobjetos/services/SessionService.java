package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class SessionService {
    @Autowired
    private EntityManager em;

    private Session get(long id){
        return this.em.find(Session.class, id);
    }

    private void save(long id) {
        Session session = new Session();

        session.setId(id);
        session.setUltimoAcceso(new Date());

        this.em.persist(session);
    }

    public Session getOrSave(long id){
        Session session = this.get(id);

        if(session == null){
            this.save(id);
            session = this.get(id);
        }

        return session;
    }
}
