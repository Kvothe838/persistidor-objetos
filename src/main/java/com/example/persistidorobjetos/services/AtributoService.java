package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.model.Atributo;
import com.example.persistidorobjetos.model.TipoAtributo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class AtributoService {
    @Autowired
    private GenericService genericService;
    @Autowired
    private TipoAtributoService tipoAtributoService;

    public ArrayList<Atributo> generarAtributos(Object o) {
        Class<?> clazz = o.getClass();
        ArrayList<Atributo> atributos = (ArrayList<Atributo>) Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> this.genericService.esPersisible(field, clazz))
                .map(field -> {
                    Atributo atributo = new Atributo();

                    atributo.setNombre(field.getName());

                    TipoAtributo tipoAtributo = this.tipoAtributoService.getOrSave(field);
                    atributo.setTipoAtributo(tipoAtributo);

                    return atributo;
                }).collect(Collectors.toList());

        return atributos;
    }
}
