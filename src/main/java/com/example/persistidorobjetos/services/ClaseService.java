package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClaseService {
    @Autowired
    private EntityManager em;
    @Autowired
    private GenericService genericService;

    public Clase get(String nombre, Integer sessionId){
        try{
            Query query;
            String queryStr;

            if(sessionId == null){
                queryStr = "select c from Clase c where c.nombre = :nombre AND c.session is null";
                query = this.em.createQuery(queryStr)
                        .setParameter("nombre", nombre);
            } else {
                queryStr = "select c from Clase c where c.nombre = :nombre AND c.session.id = :sessionId";
                query = this.em.createQuery(queryStr)
                        .setParameter("nombre", nombre)
                        .setParameter("sessionId", sessionId);
            }

            return (Clase) query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
    }

    public Clase get(String nombre){
        try{
            String queryStr = "select c from Clase c where c.nombre = :nombre";
            Query query = this.em.createQuery(queryStr)
                    .setParameter("nombre", nombre);

            return (Clase) query.getSingleResult();
        } catch(NoResultException e){
            return null;
        }
    }

    public void save(String nombre, Session session) {
        Clase clase = new Clase();

        clase.setSession(session);
        clase.setNombre(nombre);

        this.em.persist(clase);
    }

    public Clase getOrSave(String clazzName, Session session) {
        Clase clase = session == null ? this.get(clazzName) : this.get(clazzName, session.getId());

        if(clase == null){
            this.save(clazzName, session);
            clase = session == null ? this.get(clazzName) : this.get(clazzName, session.getId());
        }

        return clase;
    }

    public void borrarAtributosEInstancias(Clase clase, Class<?> clazz) {
        if(clase.getAtributos() == null){
            return;
        }

        if(this.sonIguales(clase, clazz)){
            List<Instancia> instanciasNoPrincipales = clase.getInstancias().stream().filter(instancia -> !instancia.isPrincipal()).collect(Collectors.toList());
            clase.setInstancias(instanciasNoPrincipales);
        } else {
            clase.setInstancias(null);
            clase.setAtributos(null);
        }
    }

    private boolean sonIguales(Clase clase, Class<?> clazz){
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> this.genericService.esPersisible(field, clazz)).collect(Collectors.toList());
        List<Atributo> atributos = clase.getAtributos();

        boolean estanAtributosEnFields = atributos.stream().allMatch(atributo -> fields.stream()
                .anyMatch(field -> this.sonIguales(atributo, field)));

        if(!estanAtributosEnFields){
            return false;
        }

        boolean estanFieldsEnAtributos = fields.stream().allMatch(field -> atributos.stream()
                .anyMatch(atributo -> this.sonIguales(atributo, field)));

        return estanFieldsEnAtributos;
    }

    private boolean sonIguales(Atributo atributo, Field field){
        return Objects.equals(atributo.getNombre(), field.getName())
                && Objects.equals(atributo.getTipoAtributo().getNombre(), field.getType().getName());
    }
}
