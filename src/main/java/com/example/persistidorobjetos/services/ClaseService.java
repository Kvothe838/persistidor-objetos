package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;

    public Clase getClaseByNombre(String nombre) {
        String hql = "SELECT c.id FROM Clase c WHERE c.nombre =:nombre";
        Query q = this.em.createQuery(hql);
        q.setParameter("nombre", nombre);
        return (Clase) q.getSingleResult();
    }

    public void saveClase(String nombre){
        Clase nuevaClase = new Clase();
        nuevaClase.setNombre(nombre);
        this.em.persist(nuevaClase);
    }
}
