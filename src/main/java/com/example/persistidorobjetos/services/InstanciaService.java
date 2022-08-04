package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Instancia;
import com.example.persistidorobjetos.model.ValorAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstanciaService {
    @Autowired
    ValorAtributoService valorAtributoService;
    @Autowired
    EntityManager entityManager;

    public Instancia generarInstanciaPrincipal(Object o, ArrayList<Atributo> atributos){
        Instancia instancia = new Instancia();
        ArrayList<ValorAtributo> valorAtributos = this.valorAtributoService.generarValores(o, atributos);

        instancia.setPrincipal(true);
        instancia.setAtributos(valorAtributos);

        this.entityManager.persist(instancia);
        this.entityManager.flush();

        return instancia;
    }
}
