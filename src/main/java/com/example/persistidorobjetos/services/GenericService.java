package com.example.persistidorobjetos.services;

import com.example.persistidorobjetos.annotations.NotPersistable;
import com.example.persistidorobjetos.annotations.Persistable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class GenericService {
    public boolean esPersisible(Field field, Class<?> clazz){
        return field.isAnnotationPresent(Persistable.class) ||
                (clazz.isAnnotationPresent(Persistable.class) && !field.isAnnotationPresent(NotPersistable.class));
    }
}
