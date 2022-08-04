package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.Session;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PersistentObjectService {
    @Autowired
    SessionService sessionService;
    @Autowired
    ClaseService claseService;
    @Autowired
    AtributoService atributoService;
    @Autowired
    InstanciaService instanciaService;

    public boolean store(Long sId, Object o) {
        if(o == null){
            throw new NotYetImplementedException();
        }

        boolean esExterno = sId != null;

        if(esExterno){
            Session session = this.sessionService.getOrSave(sId);

            Class<?> clazz = o.getClass();
            String clazzName = clazz.getName();
            Clase clase = this.claseService.getOrSave(clazzName, session);

            this.claseService.borrarAtributosEInstancias(clase, clazz);

            ArrayList<Atributo> atributos = this.atributoService.generarAtributos(o);
            clase.setAtributos(atributos);

            Instancia instanciaPrincipal = this.instanciaService.generarInstanciaPrincipal(o, atributos);
            clase.agregarInstanciaPrincipal(instanciaPrincipal);
        }

        return false;
    }
}
