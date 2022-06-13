package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.TipoAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class AtributoService {
    @Autowired
    private TipoAtributoService tipoAtributoService;
    @Autowired
    private EntityManager em;

    public Atributo getAtributo(Field field){
        Atributo atributo = new Atributo();
        atributo.setNombre(field.getName());
        TipoAtributo tipoAtributo = this.tipoAtributoService.getTipoAtributo(field.getType().getName());
        atributo.setTipoAtributo(tipoAtributo);

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
