package com.alura.literalura.main;

import com.alura.literalura.model.*;
import com.alura.literalura.service.*;
import com.alura.literalura.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String URL = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvertirDatos conversor = new ConvertirDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Scanner scanner = new Scanner(System.in);
    private Integer opcion = -1;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu() {
        System.out.println("""
                ------------------------
                Escoge una opción
                \n
                1- Buscar libro por titulo
                2- Ver libros registrados
                3- Ver autores registrados
                4- Ver autores vivos en un determinado año
                5- Ver libros registrados por idioma
                \n
                0- Salir
                """);
    }

    private DatosLibro buscarLibro(String tituloLibro) {
        //Realizamos consulta a la API
        String json = consumoApi.obtenerDatos(URL + tituloLibro.replace(" ", "+"));
        //Creamos una lista donde almacenamos la respuesta del JSON con los campos del Libro
        List<DatosLibro> libros = conversor.cambiaJsonClase(json, DatosIniciales.class).resultadosLibro();
        //De nuestra lista filtramos los resultados para obtener el titulo que tenga el nombre buscado
        Optional<DatosLibro> libro = libros.stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                .findFirst();
        if (libro.isPresent()) {
            return libro.get();
        }
        System.out.println("El libro no ha sido encontrado");
        return null;
    }


    private void leerLibro(Libro libro) {
        System.out.println("--------+ LIBRO +--------");
        System.out.println("Titulo: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor().getNombre());
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Descargas: " + libro.getDescargas() + "\n");
    }

    private void leerAutor(Autor autor) {
        System.out.println("------------------------");
        System.out.println("Autor: " + autor.getNombre());
        System.out.println("Fecha de nacimiento: " + autor.getAnioNacimiento());
        System.out.println("Fecha de fallecimiento: " + autor.getAnioMuerte());
        List<String> libros = autor.getLibros().stream()
                .map(l -> l.getTitulo())
                .collect(Collectors.toList());
        System.out.println("Libros: " + libros + "\n");
    }

    public void Programa() {
        while (opcion != 0) {
            menu();
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el nombre del libro que desea buscar:");
                    String tituloLibro = scanner.nextLine();
                    Libro libro = new Libro(buscarLibro(tituloLibro));
                    leerLibro(libro);
                    libroRepository.save(libro);
                    break;
                case 2:
                    List<Libro> libros = libroRepository.findAll();
                    libros.stream()
                            .forEach(this::leerLibro);
                    break;
                case 3:
                    List<Autor> autores = autorRepository.findAll();
                    autores.stream()
                            .forEach(this::leerAutor);
                    break;
                case 4:
                    System.out.println("Ingresa el año vivo de autor(es) que desea buscar");
                    Integer anioMuerte = scanner.nextInt();
                    autores = autorRepository.findByAnioMuerteGreaterThan(anioMuerte);
                    autores.stream()
                            .forEach(this::leerAutor);
                    break;
                case 5:
                    System.out.println("""
                    Ingrese el idioma para buscar los libros:
                    es - Español
                    en - Ingles
                    fr - Frances
                    pt - Portugues
                    """);
                    String idioma = scanner.next();
                    libros = libroRepository.findByIdioma(idioma);
                    libros.stream()
                            .forEach(this::leerLibro);
                    break;
            }
        }
    }
}