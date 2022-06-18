package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class SessionService {
    @Autowired
    EntityManager em;

    @Transactional
    public void saveSession(long sessionId, Date ultimoAcceso){
        Session session = new Session();

        session.setId(sessionId);
        session.setUltimoAcceso(ultimoAcceso);

        this.em.persist(session);
    }

    public Session getSession(long sessionId){
        return this.em.find(Session.class, sessionId);
    }

    @Transactional
    public void updateSession(Session session, Date ultimoAcceso){
        session.setUltimoAcceso(ultimoAcceso);

        this.em.merge(session);
    }

    @Transactional
    public void saveOrUpdateSession(long sessionId){
        Session session = this.getSession(sessionId);
        Date now = new Date();

        if(session == null){
            this.saveSession(sessionId, now);
        } else {
            this.updateSession(session, now);
        }
    }
}
