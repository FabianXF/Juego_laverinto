package com.laberinto.service;

import com.laberinto.dto.RankingDTO;
import com.laberinto.model.Ranking;
import com.laberinto.model.Usuario;
import com.laberinto.repository.RankingRepository;
import com.laberinto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo tiempo de un jugador en un nivel
     * Ahora guarda TODOS los intentos, no solo el mejor
     */
    @Transactional
    public Ranking registrarTiempo(Long usuarioId, Integer nivel, Integer tiempo, Integer pasos, Integer puntuacion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Siempre crear un nuevo registro (no actualizar)
        Ranking nuevoRanking = new Ranking(usuario, nivel, tiempo, pasos, puntuacion);
        return rankingRepository.save(nuevoRanking);
    }

    /**
     * Obtiene el top N de tiempos por nivel ordenados por mejor tiempo
     * Ahora puede incluir múltiples entradas del mismo usuario
     */
    public List<RankingDTO> getTopPorNivel(Integer nivel, Integer limit) {
        List<Ranking> rankings = rankingRepository.findTopByNivelOrderByMejorTiempo(nivel);

        // Limitar resultados
        int maxResults = Math.min(limit, rankings.size());
        List<RankingDTO> resultado = new ArrayList<>();

        for (int i = 0; i < maxResults; i++) {
            Ranking r = rankings.get(i);
            RankingDTO dto = convertirADTO(r);
            dto.setPosicion((long) (i + 1));
            resultado.add(dto);
        }

        return resultado;
    }

    /**
     * Obtiene la mejor posición de un usuario en un nivel específico
     */
    public Long getPosicionUsuario(Long usuarioId, Integer nivel) {
        // Obtener todos los rankings del usuario en ese nivel
        List<Ranking> userRankings = rankingRepository.findByUsuarioIdOrderByNivelAsc(usuarioId);

        // Filtrar por nivel y encontrar el mejor tiempo
        Optional<Ranking> bestRanking = userRankings.stream()
                .filter(r -> r.getNivel().equals(nivel))
                .min((r1, r2) -> r1.getMejorTiempo().compareTo(r2.getMejorTiempo()));

        if (bestRanking.isPresent()) {
            return rankingRepository.findPosicionByNivelAndTiempo(nivel, bestRanking.get().getMejorTiempo());
        }

        return null;
    }

    /**
     * Obtiene el mejor ranking de cada nivel para un usuario
     */
    public List<RankingDTO> getRankingsPorUsuario(Long usuarioId) {
        List<Ranking> rankings = rankingRepository.findByUsuarioIdOrderByNivelAsc(usuarioId);
        List<RankingDTO> resultado = new ArrayList<>();

        // Agrupar por nivel y tomar solo el mejor de cada nivel
        Map<Integer, Ranking> mejoresPorNivel = new HashMap<>();
        for (Ranking r : rankings) {
            Integer nivel = r.getNivel();
            if (!mejoresPorNivel.containsKey(nivel) ||
                    r.getMejorTiempo() < mejoresPorNivel.get(nivel).getMejorTiempo()) {
                mejoresPorNivel.put(nivel, r);
            }
        }

        // Convertir a DTO
        for (Ranking r : mejoresPorNivel.values()) {
            RankingDTO dto = convertirADTO(r);
            dto.setPosicion(rankingRepository.findPosicionByNivelAndTiempo(r.getNivel(), r.getMejorTiempo()));
            resultado.add(dto);
        }

        return resultado;
    }

    /**
     * Convierte una entidad Ranking a DTO
     */
    private RankingDTO convertirADTO(Ranking ranking) {
        return new RankingDTO(
                ranking.getId(),
                ranking.getUsuario().getId(),
                ranking.getUsuario().getNombre(),
                ranking.getNivel(),
                ranking.getMejorTiempo(),
                ranking.getPasosUsados(),
                ranking.getPuntuacion(),
                ranking.getFechaLogro());
    }
}
