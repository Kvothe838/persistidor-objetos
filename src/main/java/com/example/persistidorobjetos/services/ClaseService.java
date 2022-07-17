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
import com.example.persistidorobjetos.model.Instancia;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;
    
    @Autowired
    private AtributoService atributoService;
    @Autowired 
    private InstanciaService instanciaService;

    @Transactional
    public Clase getClaseByNombre(String nombre) {
        String hql = "SELECT c.id FROM Clase c WHERE c.nombre =:nombre";
        Query q = this.em.createNativeQuery(hql);
        q.setParameter("nombre", nombre);
        try{
        	Integer id = (Integer) q.getSingleResult();
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

//		TODO no se estan borrando las instancias antes de hacer el merge de las clases, va a tirar conflicto 
//    public void updateClase(Class<?> clazz){
//        Clase clasePersistida = this.getClaseByNombre(clazz.getName());
//        Clase nuevaClase = this.generateClaseObject(clazz);
//
//        List<Integer> idAtributosPersistidosOrdenados = clasePersistida.getAtributos().stream().map(Atributo::getId).sorted().collect(Collectors.toList());
//        List<Integer> idAtributosNuevosOrdenados = nuevaClase.getAtributos().stream().map(Atributo::getId).sorted().collect(Collectors.toList());
//
//        boolean sonAtributosIguales = idAtributosPersistidosOrdenados.equals(idAtributosNuevosOrdenados);
//
//        if(sonAtributosIguales){
//            return;
//        }
//
//        clasePersistida.setAtributos(nuevaClase.getAtributos());
//        this.em.merge(clasePersistida);
//    }
    

    @Transactional
	public Clase updateClase(Class<?> clazz){
		Clase claseEnBD = getClaseByNombre(clazz.getName());
		Clase claseNueva = generateClaseObject(clazz);
		if(!claseEnBD.equals(claseNueva)){
			claseEnBD.getAtributos().clear();
			claseEnBD.getAtributos().addAll(claseNueva.getAtributos());
			deleteInstanciasOfClase(clazz);
			em.merge(claseEnBD);
		}
		return claseEnBD;
    }

    public Clase getClase(Class<?> clazz){
        return this.getClaseByNombre(clazz.getName());
    }
    
    private void deleteInstanciasOfClase(Class<?> clazz){
    	String hql = "SELECT i.id FROM instancia i inner join clase c on i.clase_id = c.id WHERE c.nombre =:clase";
        Query q = this.em.createNativeQuery(hql);
        q.setParameter("clase", clazz.getName());
		List<Integer> idsInstancias = (List<Integer>) q.getResultList();
		for(Integer idInstancia : idsInstancias){
			Instancia instancia = em.find(Instancia.class, idInstancia);
			em.remove(instancia);
		}
    }

    
}
