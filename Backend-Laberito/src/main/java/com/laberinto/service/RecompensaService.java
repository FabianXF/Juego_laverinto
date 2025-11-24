package com.laberinto.service;

import com.laberinto.dto.RecompensaDTO;
import com.laberinto.model.Recompensa;
import com.laberinto.model.Usuario;
import com.laberinto.repository.RecompensaRepository;
import com.laberinto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecompensaService {

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Otorga una recompensa a un usuario
     */
    @Transactional
    public Recompensa otorgarRecompensa(Long usuarioId, String tipo, Integer nivel, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Recompensa recompensa = new Recompensa(usuario, tipo, nivel, descripcion);
        return recompensaRepository.save(recompensa);
    }

    /**
     * Obtiene todas las recompensas de un usuario
     */
    public List<RecompensaDTO> getRecompensasPorUsuario(Long usuarioId) {
        List<Recompensa> recompensas = recompensaRepository.findByUsuarioIdOrderByFechaObtencionDesc(usuarioId);
        List<RecompensaDTO> resultado = new ArrayList<>();

        for (Recompensa r : recompensas) {
            resultado.add(convertirADTO(r));
        }

        return resultado;
    }

    /**
     * Determina si un jugador merece una recompensa basado en su desempeño
     * Retorna el tipo de recompensa o null si no merece ninguna
     */
    public String determinarRecompensa(Integer tiempo, Integer pasosUsados, Integer tiempoOptimo,
            Integer pasosOptimos) {
        // Medalla de oro: completar en tiempo óptimo o mejor
        if (tiempo <= tiempoOptimo && pasosUsados <= pasosOptimos) {
            return "gold_medal";
        }

        // Medalla de plata: completar en menos del 120% del tiempo óptimo
        if (tiempo <= tiempoOptimo * 1.2) {
            return "silver_medal";
        }

        // Medalla de bronce: completar en menos del 150% del tiempo óptimo
        if (tiempo <= tiempoOptimo * 1.5) {
            return "bronze_medal";
        }

        // Maestro de velocidad: completar en menos pasos que el óptimo
        if (pasosUsados < pasosOptimos) {
            return "speed_master";
        }

        // Ejecución perfecta: completar en pasos óptimos exactos
        if (pasosUsados == pasosOptimos) {
            return "perfect_run";
        }

        return null; // No merece recompensa
    }

    /**
     * Obtiene la descripción de una recompensa según su tipo
     */
    public String getDescripcionRecompensa(String tipo) {
        switch (tipo) {
            case "gold_medal":
                return "¡Medalla de Oro! Completaste el nivel en tiempo récord";
            case "silver_medal":
                return "¡Medalla de Plata! Excelente tiempo";
            case "bronze_medal":
                return "¡Medalla de Bronce! Buen trabajo";
            case "speed_master":
                return "¡Maestro de la Velocidad! Menos pasos que el óptimo";
            case "perfect_run":
                return "¡Ejecución Perfecta! Pasos óptimos exactos";
            default:
                return "Recompensa obtenida";
        }
    }

    /**
     * Convierte una entidad Recompensa a DTO
     */
    private RecompensaDTO convertirADTO(Recompensa recompensa) {
        return new RecompensaDTO(
                recompensa.getId(),
                recompensa.getTipo(),
                recompensa.getNivel(),
                recompensa.getDescripcion(),
                recompensa.getFechaObtencion());
    }
}
