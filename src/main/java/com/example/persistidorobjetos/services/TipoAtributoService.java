package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.TipoAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class TipoAtributoService {
    @Autowired
    private EntityManager em;
    @Autowired
    @Lazy
    private ClaseService claseService;

    private TipoAtributo get(String nombre){
        try{
            String queryStr = "select ta from TipoAtributo ta where ta.nombre = :nombre";
            Query query = this.em.createQuery(queryStr)
                    .setParameter("nombre", nombre);

            return (TipoAtributo) query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
    }

    private void save(Field field){
        TipoAtributo tipoAtributo = new TipoAtributo();
        Type typeTipoAtributo = field.getType();

        tipoAtributo.setNombre(typeTipoAtributo.getTypeName());

        boolean esLista = typeTipoAtributo.equals(List.class);

        if(esLista){
            Class<?> claseInterna = (Class) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
            Clase clase = this.claseService.getOrSave(claseInterna.getName(), null);

            tipoAtributo.setClase(clase);
        }

        this.em.persist(tipoAtributo);
    }
    public TipoAtributo getOrSave(Field field) {
        String typeName = field.getType().getName();
        TipoAtributo tipoAtributo = this.get(typeName);

        if(tipoAtributo == null){
            this.save(field);
            tipoAtributo = this.get(typeName);
        }

        return tipoAtributo;
    }
}
