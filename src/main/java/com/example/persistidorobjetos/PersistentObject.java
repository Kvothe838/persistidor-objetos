package com.example.persistidorobjetos;

public class PersistentObject
{
    // Almacena la instancia del objeto o asociada a la clave sId,
    // o actualiza la instancia existente retornando true o false
    // segun actualiza o almacena.
    // El objeto o puede ser null, en tal caso el valor que se
    // almacenara sera null.
    /*public boolean store(long sId,Object o){
    };*/
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
