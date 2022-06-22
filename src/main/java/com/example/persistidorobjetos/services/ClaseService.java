package com.example.persistidorobjetos.services;

import java.lang.reflect.Field;
import java.math.BigInteger;
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

    @Transactional
    public Clase getClaseByNombre(String nombre) {
        String hql = "SELECT c.id FROM Clase c WHERE c.nombre =:nombre";
        Query q = this.em.createNativeQuery(hql);
        q.setParameter("nombre", nombre);
        try{
        	BigInteger id = (BigInteger) q.getSingleResult();
        	Clase clase = em.find(Clase.class, id.longValue());
        	return clase;
        }catch(NoResultException e){
        	return null;
        }
    }

    @Transactional
    public Clase saveClase(Class<?> clazz){
        Clase nuevaClase = this.generateClaseObject(clazz);
        this.em.persist(nuevaClase);
        return nuevaClase;
    }
    
    public Clase generateClaseObject(Class<?> clazz){
    	if(isClasePersistable(clazz)){
    		Clase clase = new Clase();
    		clase.setNombre(clazz.getName());
    		
    		List<Atributo> atributos = new ArrayList<Atributo>();
    		for(Field field : clazz.getDeclaredFields()){
    			if(field.isAnnotationPresent(Persistable.class) ||
    					(clazz.isAnnotationPresent(Persistable.class) && !field.isAnnotationPresent(NotPersistable.class))){
    				Atributo atributo = this.atributoService.generateAtributoObject(field);
    				atributos.add(atributo);
    			}
    		}
    		clase.setAtributos(atributos);    		
    		return clase;
    	}
    	return null;
    }
    
    public Boolean isClasePersistable(Class<?> clazz){
    	if(clazz.isAnnotationPresent(Persistable.class))
    		return true;
    	for(Field field : clazz.getDeclaredFields()){
    		if(field.isAnnotationPresent(Persistable.class))
    			return true;
    	}
    	return false;
    }
    
    public Boolean isClaseStored(Class<?> clazz){
    	String hql = "SELECT COUNT(*) FROM Clase c WHERE c.nombre = :nombre";
        Query q = this.em.createNativeQuery(hql);
        q.setParameter("nombre", clazz.getName());
        BigInteger result = (BigInteger) q.getSingleResult();
        return result.intValue() == 1;
    }
    
}
