package com.alura.literalura.DTO;

public record AutorDTO (
        //@JsonAlias("id") Long id,
        String nombre,
        Integer anioNacimiento,
        Integer anioMuerte){
}
