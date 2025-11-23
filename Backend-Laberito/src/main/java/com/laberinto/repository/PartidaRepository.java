package com.laberinto.repository;

import com.laberinto.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByUsuarioId(Long usuarioId);
}
