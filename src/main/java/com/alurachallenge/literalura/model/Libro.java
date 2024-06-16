package com.alurachallenge.literalura.model;

import jakarta.persistence.*;

import java.util.OptionalDouble;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    private String idiomas;
    private Double numeroDescargas;
    @ManyToOne
    private Autor autor;

    //Constructors-----------------------------------------------------------------------------------
    public Libro() {
    }

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = datosLibros.idiomas().get(0); //!= null && !datosLibros.idiomas().isEmpty() ? String.join(",", datosLibros.idiomas()) : null;
        this.numeroDescargas = OptionalDouble.of(datosLibros.nomeroDeDescargas()).orElse(0);
    }

    //Getters & Setters------------------------------------------------------------------------------

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    //Methods-------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "------ Libro ------\n"+
                "Título: " + titulo + "\n" +
                "Autor: " + autor.getNombre() + "\n" +
                "Idiomas: " + idiomas + "\n" +
                "Número de descargas: " + numeroDescargas + "\n" +
                "-------------------";
    }
}

