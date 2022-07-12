package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.TipoAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Optional;

@Service
public class TipoAtributoService {
    @Autowired
    private EntityManager em;

    public TipoAtributo getTipoAtributo(String nombreTipoAtributo){
        TipoAtributo tipoAtributo = this.getTipoAtributoByNombre(nombreTipoAtributo);

        if(tipoAtributo == null){
            tipoAtributo = new TipoAtributo();
            tipoAtributo.setNombre(nombreTipoAtributo);
            em.persist(tipoAtributo);
        }

        return tipoAtributo;
    }

    private TipoAtributo getTipoAtributoByNombre(String nombre) {
        String hql = "SELECT * FROM tipo_atributo WHERE nombre =:nombre";
        Query q = this.em.createNativeQuery(hql, TipoAtributo.class);
        q.setParameter("nombre", nombre);

        try{
            return (TipoAtributo) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
