package com.example.persistidorobjetos;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.Session;
import com.example.persistidorobjetos.services.ClaseService;
import com.example.persistidorobjetos.services.InstanciaService;
import com.example.persistidorobjetos.services.SessionService;

@Component
public class PersistentObject
{
    @Autowired
    private ClaseService claseService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private InstanciaService instanciaService;
	
    // Almacena la instancia del objeto o asociada a la clave sId,
    // o actualiza la instancia existente retornando true o false
    // segun actualiza o almacena.
    // El objeto o puede ser null, en tal caso el valor que se
    // almacenara sera null.
    public boolean store(long sId, Object o) throws Exception {
		if(o == null){
			throw new NotYetImplementedException();
		}
		
		sessionService.saveOrUpdateSession(sId);

    	Class<?> clazz = o.getClass();

	    if(this.claseService.isClasePersistable(clazz)){
	    	System.out.println("La clase es persistible, se procede a persistir el objeto");
	    	//se verifica la existencia de la clase en DB y se crea junto con sus atributos
	    	Clase clase = null;
	    	if (claseService.isClaseStored(clazz)){
	    		clase = claseService.updateClase(clazz);	
	    	}else{
	    		clase = this.claseService.saveClase(clazz);
	    	}
	    	Session session = sessionService.getSession(sId);
	    	if(instanciaService.existsInstanciaByClaseAndSession(sId, clazz.getName())){
	    		instanciaService.updateInstancia(clase, o, session);
	    	}else{
	    		Instancia instancia = instanciaService.generateInstancia(clase, o, session);	    		
	    		instanciaService.saveInstancia(instancia);
	    	}
        }else{
			throw new Exception("Clase no persistible");
        }

	    return true;
    };
    
    
    // Devuelve la instancia del objeto o asociada a la clave sId.
    // Si la clase no se encuentra persistida en la base de datos, o no hay una instancia
    //	asociada a la clase y a ese sessionId, devuelve null
    public <T> T load(long sId,Class<T> clazz) throws Exception{
    	T objectToReturn = (T) instanciaService.loadObject(sId, clazz);
    	return objectToReturn;
    };
    // Retorna true o false según exista o no una instancia
    // de clazz (aunque sea null) asociada a la clave sId.
    public boolean exists(long sId,Class<?> clazz){
    	return instanciaService.existsInstanciaByClaseAndSession(sId, clazz.getName());
    };
    // Retorna (en milisegundos) el tiempo transcurrido
    // desde el ultimo acceso registrado para la clave sId,
    // sin considerar las llamadas a este metodo ni a exists.
    public long elapsedTime(long sId){
    	return sessionService.elapsedTime(sId);
    };
    // retorna y elimina la instancia de clazz vinculada a la
    // clave sId, o retorna null si no existe dicha instancia
    public <T> T delete(long sId,Class<T> clazz) throws Exception{
    	T objectToReturn = (T) instanciaService.deleteInstance(sId, clazz);
    	return objectToReturn;
    };
}
