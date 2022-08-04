package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.Clase;
import com.example.persistidorobjetos.model.TipoAtributo;
import com.example.persistidorobjetos.model.ValorAtributo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValorAtributoService {
    @Autowired
    @Lazy
    PersistentObjectService persistentObjectService;
    @Autowired
    ClaseService claseService;
    public ArrayList<ValorAtributo> generarValores(Object o, ArrayList<Atributo> atributos){
        return (ArrayList<ValorAtributo>) atributos.stream().map(atributo -> this.generarValorAtributo(o, atributo)).collect(Collectors.toList());
    }

    private ValorAtributo generarValorAtributo(Object o, Atributo atributo){
        try {
            Object valor = PropertyUtils.getProperty(o, atributo.getNombre());
            ValorAtributo valorAtributo = new ValorAtributo();

            valorAtributo.setAtributo(atributo);

            if(valor instanceof List<?>){
                List list = (List) valor;
                boolean esTipoPrimitivo = atributo.getTipoAtributo().getClase() == null;
                ArrayList<String> valores;

                if(esTipoPrimitivo){
                    valores = (ArrayList<String>) list.stream().map(elemento -> String.valueOf(elemento)).collect(Collectors.toList());
                    System.out.println(valores);
                } else {
                    list.forEach(elemento -> {
                        this.persistentObjectService.store(null, elemento);
                    });

                    Clase clase = this.claseService.get(list.get(0).getClass().getName(), null);

                    valores = (ArrayList<String>) clase.getInstancias().stream().map(instancia -> String.valueOf(instancia.getId()));
                }

                valorAtributo.setValores(valores);

            } else {
                valorAtributo.setValor(String.valueOf(valor));
            }

            return valorAtributo;
        } catch(Exception e){
            return null;
        }
    }
}
