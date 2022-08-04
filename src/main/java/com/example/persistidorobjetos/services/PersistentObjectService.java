package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Session;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistentObjectService {
    @Autowired
    SessionService sessionService;
    @Autowired
    ClaseService claseService;

    public boolean store(long sId, Object o) {
        if(o == null){
            throw new NotYetImplementedException();
        }

        Session session = this.sessionService.getOrSave(sId);

        Class<?> clazz = o.getClass();
        String clazzName = clazz.getName();
        Clase clase = this.claseService.getOrSave(clazzName, session);

        this.claseService.borrarAtributos(clase);

        return false;
    }
}
