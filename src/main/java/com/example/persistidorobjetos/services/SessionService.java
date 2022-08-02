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
    public void saveSession(long sessionId){
        Session session = new Session();

        session.setId(sessionId);
        session.setUltimoAcceso(new Date());

        this.em.persist(session);
    }

    public Session getSession(long sessionId){
        return this.em.find(Session.class, sessionId);
    }

    @Transactional
    public void updateSession(Session session){
        session.setUltimoAcceso(new Date());

        this.em.merge(session);
    }

    @Transactional
    public void saveOrUpdateSession(long sessionId){
        Session session = this.getSession(sessionId);

        if(session == null){
            this.saveSession(sessionId);
        } else {
            this.updateSession(session);
        }
    }
    
    public long elapsedTime(long sId){
    	Session session = this.getSession(sId);
    	long ultimoAccesoEnMilli = session.getUltimoAcceso().toInstant().toEpochMilli();
    	return new Date().toInstant().toEpochMilli() - ultimoAccesoEnMilli;
    }
}
