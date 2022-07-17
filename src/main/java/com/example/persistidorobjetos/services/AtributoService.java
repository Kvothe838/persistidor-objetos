package com.example.persistidorobjetos.services;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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
        TipoAtributo tipoAtributo = this.tipoAtributoService.getTipoAtributo(field.getType().getName());

        if(field.getClass().equals(ArrayList.class)){
        	Class<?> innerClazz = (Class) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
        	int i = 1+1;
        }
        
        Clase clase = this.claseService.getClaseByNombre(field.getType().getName());
        if(clase == null){
            clase = claseService.generateClaseObject(field.getType());
        }
        
        

//        Atributo atributo = this.getAtributo(field.getName(), clase, tipoAtributo);

//        if(atributo == null){
        	Atributo atributo = new Atributo();
            atributo.setNombre(field.getName());
            atributo.setTipoAtributo(tipoAtributo);
//        }

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

    private Atributo getAtributo(String nombre, Clase clase, TipoAtributo tipoAtributo){
        String hql = "SELECT a.id, a.nombre, a.clase_id, a.tipo_atributo_id FROM Atributo a WHERE a.nombre =:nombre AND a.tipo_atributo_id =:tipoAtributoId";

        if(clase != null){
            hql += " AND a.clase_id =:claseId";
        }

        Query q = this.em.createNativeQuery(hql);
        q.setParameter("nombre", nombre);
        q.setParameter("tipoAtributoId", tipoAtributo.getId());

        if(clase != null){
            q.setParameter("claseId", clase.getId());
        }

        try{
            Atributo atributo = (Atributo)q.getSingleResult();
            return atributo;
        }catch(NoResultException e){
            return null;
        }
    }
}
