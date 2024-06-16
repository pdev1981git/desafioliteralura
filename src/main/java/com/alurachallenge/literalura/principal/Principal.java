package com.alurachallenge.literalura.principal;

import com.alurachallenge.literalura.model.*;
import com.alurachallenge.literalura.repository.AutorRepository;
import com.alurachallenge.literalura.repository.LibroRepository;
import com.alurachallenge.literalura.service.ConsumoAPI;
import com.alurachallenge.literalura.service.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private MenuPrincipal menuPrincipal = new MenuPrincipal();
    private boolean esNumeroInt = false;
    private int opcionElegida = 0;

    //Constructor------------------------------------------------------------------
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {

        while (true) {

            menuPrincipal.muestraMenu();
            while (!esNumeroInt){
                try {
                    System.out.println("a continuación elija la opción deseada: ");
                    opcionElegida = teclado.nextInt();
                    teclado.nextLine();
                    esNumeroInt = true;
                } catch (InputMismatchException e){
                    System.out.println("¡Opción erronea! Intente nuevamente");
                    teclado.nextLine();
                }

            }

            if (opcionElegida == 0){
                System.out.println("Saliendo del programa, gracias por utilizar nuestros servicios.");
                break;
            }

            switch (opcionElegida) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnUnDeterminadoAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                default:
                    System.out.println("Opción inválida. Desplegando nuevamente menu de opciones");
            }
            esNumeroInt = false;
        }
    }

    //Método Opción 1 ------------------------------------------------------------------------------------------------
    private void buscarLibroPorTitulo() {

        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);//trae JSON (con todos los libros) y convierte en Datos Lista Libros

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))//lleva cada titulo a mayusc y luego compara lo que se busca (convirtiendo lo a mayusc.)
                .findFirst();

        if (libroBuscado.isPresent()){

            DatosLibros libroEncontrado = libroBuscado.get();
            Libro libro = new Libro(libroEncontrado);

            Optional<Libro> libroYaExistente = libroRepository.findBytitulo(libroEncontrado.titulo());

            if(libroYaExistente.isPresent()){
                System.out.println("El libro ya se encuentra en la base de datos");
            }else{
                DatosAutor datosAutor = libroEncontrado.autor().get(0);
                Autor autor1 = new Autor(datosAutor);
                Optional<Autor> autorOptional = autorRepository.findByNombre(autor1.getNombre());

                if(autorOptional.isPresent()){
                    Autor autorExiste = autorOptional.get();
                    libro.setAutor(autorExiste);
                    libroRepository.save(libro);
                }else{
                    Autor nuevoAutor = autorRepository.save(autor1);
                    libro.setAutor(nuevoAutor);
                    libroRepository.save(libro);
                }
                System.out.println("***** El siguiente es le libro encontrado *****");
                System.out.println(libro.toString());
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    //Método Opción 2 ------------------------------------------------------------------------------------------------
    private void mostrarLibrosBuscados() {
        List<Libro> librosEnDataBase = libroRepository.findAll();
        if(librosEnDataBase.isEmpty()){
            System.out.println("No hay libros registrados");
        }else{
            System.out.println("Los libros registrados son: ");
            librosEnDataBase.forEach(l -> System.out.println(l.toString()));
        }
    }

    //Método Opción 3 ------------------------------------------------------------------------------------------------
    private void mostrarAutoresRegistrados() {
        List<Autor> listaDeAutores = autorRepository.findAll();
        if (listaDeAutores.isEmpty()){
            System.out.println("Todavía no hay autores registrados en la base de datos");
        }else{
            System.out.println("Los autores registrados son: ");
            listaDeAutores.forEach(a -> System.out.println(a.toString()));
        }
    }

    //Método Opción 4 ------------------------------------------------------------------------------------------------
    private void mostrarAutoresVivosEnUnDeterminadoAno() {
        System.out.println("Ingrese el año en el que desea ver, que autores eran vivos");
        int anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> listaDeAutoresVivos = autorRepository.listaAutoresVivosPorAnio(anio);
        if(listaDeAutoresVivos.isEmpty()){
            System.out.println("No hay autores registrados para ese periodo");
        }else {
            System.out.println("Los autores vivos en ese tiempo son: ");
            listaDeAutoresVivos.forEach(a -> System.out.println(a.toString()));
        }
    }

    //Método Opción 5 ------------------------------------------------------------------------------------------------
    private void listarLibrosPorIdioma() {
        var menu = """
                Idiomas para elegir libros a buscar:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """;
        System.out.println(menu);
        System.out.print("Ingrese las dos letras para el idioma elegido: ");
        var idioma = teclado.nextLine();
        List<Libro> listaDeLibrosPorIdioma = libroRepository.findByidiomas(idioma);
        if(listaDeLibrosPorIdioma.isEmpty()){
            System.out.println("no hay libros registrados en ese idioma");
        }else{
            System.out.println("Los libros registrados en ese idioma son: ");
            listaDeLibrosPorIdioma.forEach(a -> System.out.println(a.toString()));
        }
    }

}
