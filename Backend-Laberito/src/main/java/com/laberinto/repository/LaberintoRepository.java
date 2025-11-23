package com.laberinto.repository;

import com.laberinto.model.Laberinto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LaberintoRepository extends JpaRepository<Laberinto, Long> {
    Optional<Laberinto> findByPartidaId(Long partidaId);
}
