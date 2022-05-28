package com.example.persistidorobjetos;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.persistidorobjetos.annotations.Persistable;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.services.ClaseService;

@Component
public class PersistentObject
{
    @Autowired
    private ClaseService claseService;
    // Almacena la instancia del objeto o asociada a la clave sId,
    // o actualiza la instancia existente retornando true o false
    // segun actualiza o almacena.
    // El objeto o puede ser null, en tal caso el valor que se
    // almacenara sera null.
    public boolean store(long sId, Object o){
    	Class<?> clazz = o.getClass();
	    if(isPersistable(clazz)){
	    	System.out.println("La clase es persistible, se procede a persistir el objeto");

	    	String nombreClase = clazz.getSimpleName();
	
	    	//se verifica la existencia de la clase en DB y se crea junto con sus atributos
	        Clase claseExistente = this.claseService.getClaseByNombre(nombreClase);
	
	        if(claseExistente == null){
	            this.claseService.saveClase(clazz);
	            claseExistente = this.claseService.getClaseByNombre(nombreClase);
	        }
	        
	        
	        
        
        }else{
        	System.out.println("La clase no es persistible");
        }
        return true;
    };
    
    private Boolean isPersistable(Class<?> clazz){
    	if(clazz.isAnnotationPresent(Persistable.class))
    		return true;
    	for(Field field : clazz.getDeclaredFields()){
    		if(field.isAnnotationPresent(Persistable.class))
    			return true;
    	}
    	return false;
    }
    
    // Devuelve la instancia del objeto o asociada a la clave sId.
    /*public <T> T load(long sId,Class<T> clazz){ ... };*/
    // Retorna true o false según exista o un una instancia
    // de clazz (aunque sea null) asociada a la clave sId.
    /*public boolean exists(long sId,Class<T> clazz){ ... };*/
    // Retorna (en milisegundos) el tiempo transcurrido
    // desde el ultimo acceso registrado para la clave sId,
    // sin considerar las llamadas a este metodo ni a exists.
    /*public long elapsedTime(long sId){ ... };*/
    // retorna y elimina la instancia de clazz vinculada a la
    // clave sId, o retorna null si no existe dicha instancia
    /*public <T> T delete(long sId,Class<T> clazz){ ... };*/
}
