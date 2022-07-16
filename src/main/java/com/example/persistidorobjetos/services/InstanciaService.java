package com.example.persistidorobjetos.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.AtributoInstancia;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.model.ValorAtributo;

@Service
public class InstanciaService {

	@Autowired
	private EntityManager entityManager;
	
	public Instancia generateInstancia(Clase clase, Object object, Session session) 
				throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Instancia instancia = new Instancia();
		instancia.setClase(clase);
		instancia.setSession(session);
		List<AtributoInstancia> atributos = new ArrayList<>();
		for(Atributo atributo : clase.getAtributos()){
			Object objetoAtributo = PropertyUtils.getProperty(object, atributo.getNombre());
			if(objetoAtributo != null){
				AtributoInstancia atributoInstancia = generateAtributoInstancia(atributo, instancia, objetoAtributo);
				atributos.add(atributoInstancia);				
			}
		}
		instancia.setAtributos(atributos);
		return instancia;
	}
	
	public AtributoInstancia generateAtributoInstancia(Atributo atributo, Instancia instancia, Object object) 
				throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		AtributoInstancia atributoInstancia = new AtributoInstancia();
		atributoInstancia.setAtributo(atributo);
		atributoInstancia.setInstancia(instancia);
		ValorAtributo valorAtributo = new ValorAtributo();
		if(atributo.getClase() == null){
			if(object instanceof Collection<?>){
				//TODO manejo de listas y esas cosas
			}else{
				String value = String.valueOf(object);
				valorAtributo.setValor(value);
			}
		}else{
			Instancia subInstancia = generateInstancia(atributo.getClase(), object, null);
			valorAtributo.setInstancia(subInstancia);
		}
		atributoInstancia.setValorAtributo(valorAtributo);
		return atributoInstancia;
	}
	
	@Transactional
	public void saveInstancia(Instancia instancia){
		entityManager.persist(instancia);
	}
	
	public Instancia recoverInstancia(Integer idInstancia, Long idSession){
		String hql = "SELECT Count(*) FROM INSTANCIA i WHERE i.id = :idInstancia AND i.session_id = :idSession";
        Query q = this.entityManager.createNativeQuery(hql);
        q.setParameter("idInstancia", idInstancia);
        q.setParameter("idSession", idSession);
        try{
        	BigInteger count = (BigInteger) q.getSingleResult();
        	if(count.intValue() == 1){
        		Instancia instancia = entityManager.find(Instancia.class, idInstancia);
        		return instancia;        		
        	}else{
//        		TODO manejar caso de que no haya instancia con ese id y session id
        		return null;
        	}
        }catch(NoResultException e){
        	return null;
        }
//		entityManager.find(Instancia.class, idInstancia);
	}

	public void updateInstancia(Clase clase, Object o, Session session) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Instancia nuevaInstancia = this.generateInstancia(clase, o, session);
		nuevaInstancia.setId(o.hashCode());
		this.entityManager.merge(nuevaInstancia);
	}
	
}
