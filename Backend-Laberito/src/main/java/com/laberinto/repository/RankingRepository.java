package com.laberinto.repository;

import com.laberinto.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    // Encontrar el ranking de un usuario en un nivel específico
    Optional<Ranking> findByUsuarioIdAndNivel(Long usuarioId, Integer nivel);

    // Obtener top N jugadores por nivel ordenados por mejor tiempo
    @Query("SELECT r FROM Ranking r WHERE r.nivel = :nivel ORDER BY r.mejorTiempo ASC")
    List<Ranking> findTopByNivelOrderByMejorTiempo(@Param("nivel") Integer nivel);

    // Obtener top N jugadores por nivel ordenados por puntuación
    @Query("SELECT r FROM Ranking r WHERE r.nivel = :nivel ORDER BY r.puntuacion DESC")
    List<Ranking> findTopByNivelOrderByPuntuacion(@Param("nivel") Integer nivel);

    // Obtener todos los rankings de un usuario
    List<Ranking> findByUsuarioIdOrderByNivelAsc(Long usuarioId);

    // Obtener la posición de un usuario en un nivel específico
    @Query("SELECT COUNT(r) + 1 FROM Ranking r WHERE r.nivel = :nivel AND r.mejorTiempo < :tiempo")
    Long findPosicionByNivelAndTiempo(@Param("nivel") Integer nivel, @Param("tiempo") Integer tiempo);
}
