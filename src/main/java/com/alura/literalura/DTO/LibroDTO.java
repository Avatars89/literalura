package com.alura.literalura.DTO;

import java.util.ArrayList;

public record LibroDTO(
        Integer id,
        String titulo,
        ArrayList<String> autores,
        ArrayList<String> temas,
        ArrayList<String> lenguajes,
        Integer descargas){
}
