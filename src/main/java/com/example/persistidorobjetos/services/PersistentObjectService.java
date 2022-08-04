package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Session;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistentObjectService {
    @Autowired
    SessionService sessionService;


    public boolean store(long sId, Object o) {
        if(o == null){
            throw new NotYetImplementedException();
        }

        Session session = this.sessionService.getOrSave(sId);

        return false;
    }
}
