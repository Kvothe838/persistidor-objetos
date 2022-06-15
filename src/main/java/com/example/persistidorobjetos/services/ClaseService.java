package com.example.persistidorobjetos.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.annotations.NotPersistable;
import com.example.persistidorobjetos.annotations.Persistable;
import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;
    
    @Autowired
    private AtributoService atributoService;

    public Clase getClaseByNombre(String nombre) {
        String hql = "SELECT c.id, c.nombre FROM Clase c WHERE c.nombre =:nombre";
        Query q = this.em.createNativeQuery(hql, Clase.class);
        q.setParameter("nombre", nombre);
        try{
        	 return (Clase) q.getSingleResult();
        }catch(NoResultException e){
        	return null;
        }
    }

    @Transactional
    public void saveClase(Class<?> clazz){
        Clase nuevaClase = this.generateClaseObject(clazz);

        this.em.persist(nuevaClase);
    }
    
    public Clase generateClaseObject(Class<?> clazz){
    	Clase clase = new Clase();
    	clase.setNombre(clazz.getName());
    	List<Atributo> atributos = new ArrayList<Atributo>();

    	for(Field field : clazz.getDeclaredFields()){
    		if(field.isAnnotationPresent(Persistable.class) ||
    				(clazz.isAnnotationPresent(Persistable.class) && !field.isAnnotationPresent(NotPersistable.class))){
    			Atributo atributo = this.atributoService.getAtributo(field);
    			atributos.add(atributo);
    		}
    	}

    	clase.setAtributos(atributos);

    	return clase;
    }
}
