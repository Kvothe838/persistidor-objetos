package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.TipoAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;

@Service
public class AtributoService {
    @Autowired
    private EntityManager em;

    @Autowired
    private TipoAtributoService tipoAtributoService;

    @Transactional
    public Atributo saveAtributo(Field field){
        Atributo atributo = new Atributo();
        atributo.setNombre(field.getName());

        TipoAtributo tipoAtributo = this.tipoAtributoService.getOrSaveTipoAtributo(field.getType().getSimpleName());
        atributo.setTipoAtributo(tipoAtributo);

        this.em.persist(atributo);

        return atributo;
    }
}
