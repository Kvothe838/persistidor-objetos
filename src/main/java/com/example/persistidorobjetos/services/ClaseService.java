package com.example.persistidorobjetos.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.model.Clase;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;

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
    public void saveClase(String nombre){
        Clase nuevaClase = new Clase();
        nuevaClase.setNombre(nombre);
        this.em.persist(nuevaClase);
    }
}
