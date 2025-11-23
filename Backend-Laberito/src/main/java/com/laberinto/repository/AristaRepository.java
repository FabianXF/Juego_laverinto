package com.laberinto.repository;

import com.laberinto.model.Arista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AristaRepository extends JpaRepository<Arista, Long> {
    List<Arista> findByLaberintoId(Long laberintoId);
}
