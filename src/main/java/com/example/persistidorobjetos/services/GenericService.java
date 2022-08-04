package com.example.persistidorobjetos.services;

import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class GenericService {
    public Object getSimpleObjectFromString(Class<?> clazz, String valor) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getConstructor(String.class).newInstance(valor);
    }
}
