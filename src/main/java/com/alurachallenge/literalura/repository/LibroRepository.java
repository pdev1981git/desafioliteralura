package com.alurachallenge.literalura.repository;

import com.alurachallenge.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findBytitulo(String titulo);

    List<Libro> findByidiomas(String idioma);
}
