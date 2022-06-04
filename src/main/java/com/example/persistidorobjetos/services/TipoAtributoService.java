package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.TipoAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class TipoAtributoService {
    @Autowired
    private EntityManager em;

    public TipoAtributo getOrSaveTipoAtributo(String nombreTipoAtributo){
        TipoAtributo tipoAtributo = new TipoAtributo();
        tipoAtributo.setNombre(nombreTipoAtributo);

        this.em.persist(tipoAtributo);

        return tipoAtributo;
    }
}
