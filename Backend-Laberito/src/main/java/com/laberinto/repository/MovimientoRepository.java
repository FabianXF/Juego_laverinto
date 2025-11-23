package com.laberinto.repository;

import com.laberinto.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByPartidaIdOrderByOrdenAsc(Long partidaId);
}
