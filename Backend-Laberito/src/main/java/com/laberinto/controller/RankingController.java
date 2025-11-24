package com.laberinto.controller;

import com.laberinto.dto.RankingDTO;
import com.laberinto.dto.RecompensaDTO;
import com.laberinto.service.RankingService;
import com.laberinto.service.RecompensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ranking")
@CrossOrigin(origins = "*")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RecompensaService recompensaService;

    /**
     * Obtener top 10 jugadores por nivel
     * GET /api/v1/ranking/nivel/{nivelId}
     */
    @GetMapping("/nivel/{nivelId}")
    public ResponseEntity<?> getRankingPorNivel(@PathVariable Integer nivelId,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<RankingDTO> ranking = rankingService.getTopPorNivel(nivelId, limit);
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtener posición de un usuario en un nivel
     * GET /api/v1/ranking/posicion/{usuarioId}/{nivelId}
     */
    @GetMapping("/posicion/{usuarioId}/{nivelId}")
    public ResponseEntity<?> getPosicionUsuario(@PathVariable Long usuarioId,
            @PathVariable Integer nivelId) {
        try {
            Long posicion = rankingService.getPosicionUsuario(usuarioId, nivelId);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("posicion", posicion);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtener todos los rankings de un usuario
     * GET /api/v1/ranking/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getRankingsPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<RankingDTO> rankings = rankingService.getRankingsPorUsuario(usuarioId);
            return ResponseEntity.ok(rankings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtener recompensas de un usuario
     * GET /api/v1/ranking/recompensas/{usuarioId}
     */
    @GetMapping("/recompensas/{usuarioId}")
    public ResponseEntity<?> getRecompensasPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<RecompensaDTO> recompensas = recompensaService.getRecompensasPorUsuario(usuarioId);
            return ResponseEntity.ok(recompensas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
