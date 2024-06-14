package com.alura.literalura.service;

public interface IConvierteDatos {
    <T> T cambiaJsonClase(String json, Class<T> clase);
}
