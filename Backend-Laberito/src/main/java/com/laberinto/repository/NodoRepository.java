package com.laberinto.repository;

import com.laberinto.model.Nodo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NodoRepository extends JpaRepository<Nodo, Long> {
    List<Nodo> findByLaberintoId(Long laberintoId);
    Optional<Nodo> findByLaberintoIdAndFilaAndColumna(Long laberintoId, Integer fila, Integer columna);
}
