package com.example.persistidorobjetos.services;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.TipoAtributo;

@Service
public class AtributoService {
    @Autowired
    private TipoAtributoService tipoAtributoService;
    @Autowired
    private EntityManager em;
    @Autowired @Lazy
    private ClaseService claseService;

    public Atributo generateAtributoObject(Field field){
        Atributo atributo = new Atributo();
        atributo.setNombre(field.getName());
        TipoAtributo tipoAtributo = this.tipoAtributoService.getTipoAtributo(field.getType().getName());
        atributo.setTipoAtributo(tipoAtributo);
        Clase clase = claseService.generateClaseObject(field.getType());
        atributo.setClase(clase);
        return atributo;
    }

    public List<Atributo> getAll(){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Atributo> cq = cb.createQuery(Atributo.class);
        Root<Atributo> rootEntry = cq.from(Atributo.class);
        CriteriaQuery<Atributo> all = cq.select(rootEntry);
        TypedQuery<Atributo> allQuery = this.em.createQuery(all);
        return allQuery.getResultList();
    }
}
