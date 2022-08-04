package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

@Service
public class SessionService {
    @Autowired
    private EntityManager em;

    private Session get(long sId){
        try{
            String queryStr = "select s from Session s where s.sId = :sId";
            Query query = this.em.createQuery(queryStr)
                    .setParameter("sId", sId);

            return (Session) query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }

    }

    private void save(long id) {
        Session session = new Session();

        session.setSId(id);
        session.setUltimoAcceso(new Date());

        this.em.persist(session);
    }

    public Session getOrSave(long sId){
        Session session = this.get(sId);

        if(session == null){
            this.save(sId);
            session = this.get(sId);
        }

        return session;
    }
}
