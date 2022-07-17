package com.example.persistidorobjetos.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
		
		//pregunto si es collection
		if(object instanceof Collection<?>){
			//de ser collection veo si es array o set y seteo los valores de los elementos
			if(object instanceof ArrayList<?> || object instanceof HashSet<?>){
				valorAtributo.setValorAtributoList(new ArrayList<>());
				List list = (ArrayList) object;
				for(Object element : list){
					//pregunto si tiene clase persistible
					if(atributo.getClase() != null){
						//en caso de que si, creo una instancia y la seteo en valorAtributo
						Instancia instanciaElemento = generateInstancia(atributo.getClase(), element, null);
						ValorAtributo valorAtributo2 = new ValorAtributo();
						valorAtributo2.setInstancia(instanciaElemento);
						valorAtributo.getValorAtributoList().add(valorAtributo2);
					}else{
						//en caso de que no tenga clase persistible, guardo su valor de string
						String value = String.valueOf(element);
						ValorAtributo valorAtributo2 = new ValorAtributo();
						valorAtributo2.setValor(value);
						valorAtributo.getValorAtributoList().add(valorAtributo2);
					}
				}
			}			
		}else{
			//pregunto si tiene clase persistible
			if(atributo.getClase() != null){
				//en caso de que si, creo una instancia y la seteo en valorAtributo
				Instancia instanciaElemento = generateInstancia(atributo.getClase(), object, null);
				valorAtributo.setInstancia(instanciaElemento);
			}else{
				//en caso de que no tenga clase persistible, guardo su valor de string
				String value = String.valueOf(object);
				valorAtributo.setValor(value);
			}
		}
				
		atributoInstancia.setValorAtributo(valorAtributo);
		return atributoInstancia;
	}

	
	@Transactional
	public void saveInstancia(Instancia instancia){
		Instancia instanciaVieja = getInstanciaByClaseAndSession(instancia.getClase().getId(), instancia.getSession().getId());
		if(instanciaVieja != null){
			entityManager.remove(instanciaVieja);
		}
		entityManager.persist(instancia);
	}
	
	@Transactional
	public void deleteInstanciaByClaseAndSession(Long claseId, Long sessionId){
		String hql = "SELECT i.id FROM INSTANCIA i WHERE i.clase_id = :claseId AND i.session_id = :sessionId";
		Query q = this.entityManager.createNativeQuery(hql);
        q.setParameter("claseId", claseId);
        q.setParameter("sessionId", sessionId);
        try{
        	Integer instanciaId = (Integer) q.getSingleResult();
        	if(instanciaId != null){
        		Instancia instanciaBD = entityManager.find(Instancia.class, instanciaId);
        		entityManager.remove(instanciaBD);
        	}else{
//        		TODO manejar caso de que no haya instancia con ese id y session id
        	}
        }catch(NoResultException e){
//			TODO manejar excepcion
        }
	}
	
	public Instancia recoverInstancia(Integer instanciaId, Long sessionId){
//		String hql = "SELECT Count(*) FROM INSTANCIA i WHERE i.id = :instanciaId AND i.session_id = :sessionId";
//        Query q = this.entityManager.createNativeQuery(hql);
//        q.setParameter("instanciaId", instanciaId);
//        q.setParameter("sessionId", sessionId);
//        try{
//        	BigInteger count = (BigInteger) q.getSingleResult();
//        	if(count.intValue() == 1){
//        		Instancia instancia = entityManager.find(Instancia.class, instanciaId);
//        		return instancia;        		
//        	}else{
////        		TODO manejar caso de que no haya instancia con ese id y session id
//        		return null;
//        	}
//        }catch(NoResultException e){
//        	return null;
//        }
		return entityManager.find(Instancia.class, instanciaId);
	}

	public void updateInstancia(Clase clase, Object o, Session session) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Instancia nuevaInstancia = this.generateInstancia(clase, o, session);
		nuevaInstancia.setId(o.hashCode());
		this.entityManager.merge(nuevaInstancia);
	}
	
	public Instancia getInstanciaByClaseAndSession(Long claseId, Long sessionId){
		String hql = "SELECT i.id FROM INSTANCIA i WHERE i.clase_id = :claseId AND i.session_id = :sessionId";
		Query q = this.entityManager.createNativeQuery(hql);
        q.setParameter("claseId", claseId);
        q.setParameter("sessionId", sessionId);
        try{
        	Integer instanciaId = (Integer) q.getSingleResult();
        	if(instanciaId != null){
        		Instancia instancia = entityManager.find(Instancia.class, instanciaId);
        		return instancia;        		
        	}else{
//        		TODO manejar caso de que no haya instancia con ese id y session id
        		return null;
        	}
        }catch(NoResultException e){
//			TODO manejar excepcion
        	return null;
        }
	}
	
}