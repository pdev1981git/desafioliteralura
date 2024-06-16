package com.alurachallenge.literalura.model;

public class MenuPrincipal {

    public void muestraMenu(){
        System.out.println("""
                    \n
                    *************** MENU DE OPCIONES *****************
                    1 - Buscar libro por título.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados.
                    4 - listar autores vivos en un determinado año.
                    5 - listar libros por idioma.
                    
                    0 - Salir.
                    ***************************************************
                    """);
    }

}
