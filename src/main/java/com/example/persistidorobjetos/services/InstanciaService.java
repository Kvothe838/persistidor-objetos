package com.example.persistidorobjetos.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			AtributoInstancia atributoInstancia = generateAtributoInstancia(atributo, instancia, objetoAtributo);
			atributos.add(atributoInstancia);
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
			generateInstancia(atributo.getClase(), object, null);
		}
		atributoInstancia.setValorAtributo(valorAtributo);
		return atributoInstancia;
	}
	
}
