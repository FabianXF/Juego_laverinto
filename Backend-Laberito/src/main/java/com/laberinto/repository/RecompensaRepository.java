package com.laberinto.repository;

import com.laberinto.model.Recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {

    // Obtener todas las recompensas de un usuario
    List<Recompensa> findByUsuarioIdOrderByFechaObtencionDesc(Long usuarioId);

    // Obtener recompensas de un usuario en un nivel específico
    List<Recompensa> findByUsuarioIdAndNivel(Long usuarioId, Integer nivel);

    // Obtener recompensas por tipo
    List<Recompensa> findByTipo(String tipo);

    // Contar recompensas de un usuario
    Long countByUsuarioId(Long usuarioId);
}
