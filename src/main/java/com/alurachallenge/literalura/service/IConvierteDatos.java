package com.alurachallenge.literalura.service;

public interface IConvierteDatos {
    //Método que devuelve un tipo de dato genérico. Que recibe por parámetro: un string, y dato de tipo clase genérico.
    <T> T obtenerDatos(String json, Class<T> clase);
}
