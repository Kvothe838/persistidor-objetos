package com.example.persistidorobjetos.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import com.example.persistidorobjetos.exceptions.StructureChangedException;
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
	@Autowired
	private ClaseService claseService;
	@Autowired
	private GenericService genericService;
	@Autowired
	private SessionService sessionService;
	
	public Instancia generateInstancia(Clase clase, Object object, Session session) 
				throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Instancia instancia = new Instancia();
		instancia.setClase(clase);
		instancia.setSession(session);
		//en caso de que el objeto sea null, la lista de atributos queda como null, reflejando ese estado
		if(object != null){
			List<AtributoInstancia> atributos = new ArrayList<>();
			for(Atributo atributo : clase.getAtributos()){
				Object objetoAtributo = PropertyUtils.getProperty(object, atributo.getNombre());
				if(objetoAtributo != null){
					AtributoInstancia atributoInstancia = generateAtributoInstancia(atributo, instancia, objetoAtributo);
					atributos.add(atributoInstancia);				
				}
			}
			instancia.setAtributos(atributos);			
		}else{
			instancia.setAtributos(null);	
		}
		return instancia;
	}
	
	public AtributoInstancia generateAtributoInstancia(Atributo atributo, Instancia instancia, Object object) 
				throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		AtributoInstancia atributoInstancia = new AtributoInstancia();
		atributoInstancia.setAtributo(atributo);
		atributoInstancia.setInstancia(instancia);
		ValorAtributo valorAtributo = new ValorAtributo();
		
		//pregunto si es collection
		if(object instanceof List<?>){
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
		Instancia instanciaVieja = getInstanciaByClaseAndSession(instancia.getClase().getNombre(), instancia.getSession().getId());
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
		return entityManager.find(Instancia.class, instanciaId);
	public Instancia recoverInstancia(Long claseId, Long sessionId){
		String hql = "SELECT id FROM instancia WHERE session_id = :sessionId AND clase_id = :claseId";
		Query q = this.entityManager.createNativeQuery(hql);
        q.setParameter("sessionId", sessionId);
        q.setParameter("claseId", claseId);
        try{
        	Integer id = (Integer) q.getSingleResult();

			if(id == null){
				return null;
			}

			return this.entityManager.find(Instancia.class, id);
		}catch(NoResultException e){
        	return null;
        }
	}

	public void updateInstancia(Clase clase, Object o, Session session) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Instancia nuevaInstancia = this.generateInstancia(clase, o, session);
		nuevaInstancia.setId(o.hashCode());
		this.entityManager.merge(nuevaInstancia);
	}
	
	public Instancia getInstanciaByClaseAndSession(String claseNombre, Long sessionId){
		String hql = "SELECT i.id FROM INSTANCIA i inner join CLASE c on i.clase_id = c.id WHERE c.nombre = :claseNombre AND i.session_id = :sessionId";
		Query q = this.entityManager.createNativeQuery(hql);
		q.setParameter("claseNombre", claseNombre);
		q.setParameter("sessionId", sessionId);
        try{
        	Integer instanciaId = (Integer) q.getSingleResult();
        	if(instanciaId != null){
        		Instancia instancia = entityManager.find(Instancia.class, instanciaId);
        		return instancia;        		
        	}else{
        		return null;
        	}
        }catch(NoResultException e){
        	return null;
        }
	}
	
	public Object loadObject(long sId,Class<?> clazz) throws Exception {
		//compruebo que la clase este en BD y sea la misma
		if(claseService.isClaseStored(clazz)){
			Clase claseBD = claseService.getClase(clazz);
			Clase claseLoad = claseService.generateClaseObject(clazz);
			if(claseBD.equals(claseLoad)){
				Instancia instancia = this.getInstanciaByClaseAndSession(clazz.getName(), sId);
				if(instancia != null){
					//TODO consultar si esto realmente tendria que hacerse aca, o antes
					//actualizo la ultima vez que realizo una accion la session
					sessionService.updateSession(instancia.getSession());
					Object object = loadObjectFromInstancia(clazz, instancia);
					return object;
				}
			}else{
				throw new StructureChangedException("La estructura de la clase del objeto a recuperar difiere con la guardada en la Base de Datos");
			}
		}
		return null;
	}

	private Object loadObjectFromInstancia(Class<?> clazz, Instancia instancia)
			throws Exception {
		Object object = clazz.newInstance();
		if(!instancia.getAtributos().isEmpty()){
			for(AtributoInstancia atributoInstancia : instancia.getAtributos()){
				String arrayListName = "java.util.ArrayList";
				String nombreTipoAtributo = atributoInstancia.getAtributo().getTipoAtributo().getNombre();
				if(nombreTipoAtributo.startsWith(arrayListName)){
					ArrayList arrayList = new ArrayList<>();
					String tipoInterno = nombreTipoAtributo.substring(arrayListName.length()+1, nombreTipoAtributo.length()-1);
					for(ValorAtributo valorAtributo : atributoInstancia.getValorAtributo().getValorAtributoList()){
						String valor = valorAtributo.getValor();
						
						if(tipoInterno.startsWith("java.")){
							if(valor.equals("null") ){
								arrayList.add(null);
							}else{
								Class<?> clazzTipoInterno = Class.forName(tipoInterno);
								Object o = genericService.getSimpleObjectFromString(clazzTipoInterno, valor);
								arrayList.add(o);							
							}
						} else if(valorAtributo.getInstancia() == null){
							//de no haberla, el objeto deberia ser simple
							Object innerObject = getSimpleAtributeValue(valor, tipoInterno);
							arrayList.add(innerObject);
						}else{
							//de haberla, el objeto debe ser complejo
							Object innerObject = loadObjectFromInstancia(
									Class.forName(tipoInterno),
									valorAtributo.getInstancia());
							arrayList.add(innerObject);
						}
					}
					PropertyUtils.setSimpleProperty(
							object, atributoInstancia.getAtributo().getNombre(), 
							arrayList);
				}
				else{
					Object innerObject;
					String valorAtributo = atributoInstancia.getValorAtributo().getValor();
					
					if(nombreTipoAtributo.startsWith("java.")) {
						Class<?> clazzTipoInterno = Class.forName(nombreTipoAtributo);
						innerObject = genericService.getSimpleObjectFromString(clazzTipoInterno, valorAtributo);
					} else if(atributoInstancia.getValorAtributo().getInstancia() == null){
						//de no haberla, el objeto deberia ser simple
						innerObject = getSimpleAtributeValue(valorAtributo, atributoInstancia.getAtributo().getTipoAtributo().getNombre());
					}else{
						//de haberla, el objeto debe ser complejo
						innerObject = loadObjectFromInstancia(Class.forName(nombreTipoAtributo),atributoInstancia.getValorAtributo().getInstancia());
					}
					
					PropertyUtils.setSimpleProperty(
							object,
							atributoInstancia.getAtributo().getNombre(),
							innerObject);
				}
			}
			
		}else{
			return null;
		}

		return object;
	}

//	private void setAtributeValue(Object object, AtributoInstancia atributoInstancia)
//			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		switch(atributoInstancia.getAtributo().getTipoAtributo().getNombre()){
//			case "int":
//				PropertyUtils.setSimpleProperty(
//						object, 
//						atributoInstancia.getAtributo().getNombre(), 
//						new Integer(atributoInstancia.getValorAtributo().getValor()));								
//				break;
//			default:
//				PropertyUtils.setSimpleProperty(
//						object, atributoInstancia.getAtributo().getNombre(), 
//						atributoInstancia.getValorAtributo().getValor());
//				break;
//		}
//	}
	
	private Object getSimpleAtributeValue(String valor, String tipo)
			throws Exception {
		switch(tipo){
			case "byte":
				return Byte.parseByte(valor);
			case "short":
				return Short.parseShort(valor);
			case "int":
				return Integer.parseInt(valor);
			case "long":
				return Long.parseLong(valor);
			case "float":
				return Float.parseFloat(valor);
			case "double":
				return Double.parseDouble(valor);
			case "char":
				return valor.substring(0, 1);
			case "boolean":
				return Boolean.parseBoolean(valor);
			default:
				throw new Exception(String.format("Valor tipo atributo desconocido: %s", tipo));
		}
	}

	public Object deleteInstance(long sId, Class<?> clazz) throws Exception {
		Object object = this.loadObject(sId, clazz);
		Clase clase = this.claseService.getClase(clazz);
		this.deleteInstanciaByClaseAndSession(clase.getId(), sId);
		return object;
	}
		
	
}
