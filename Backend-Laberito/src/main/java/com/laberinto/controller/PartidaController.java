package com.laberinto.controller;

import com.laberinto.dto.MovimientoRequest;
import com.laberinto.dto.PartidaRequest;
import com.laberinto.model.Laberinto;
import com.laberinto.model.Partida;
import com.laberinto.repository.LaberintoRepository;
import com.laberinto.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/partidas")
@CrossOrigin(origins = "*")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private LaberintoRepository laberintoRepository;

    @PostMapping
    public ResponseEntity<?> crearPartida(@RequestBody PartidaRequest request) {
        try {
            Partida partida = partidaService.crearPartida(request.getUsuarioId(), request.getNivel());
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPartida(@PathVariable Long id) {
        try {
            Partida partida = partidaService.obtenerPartida(id);
            return ResponseEntity.ok(partida);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/laberinto")
    public ResponseEntity<?> obtenerLaberinto(@PathVariable Long id) {
        Optional<Laberinto> laberinto = laberintoRepository.findByPartidaId(id);
        if (laberinto.isPresent()) {
            return ResponseEntity.ok(laberinto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Alias para compatibilidad con frontend
    @GetMapping("/{id}/graf")
    public ResponseEntity<?> obtenerGrafo(@PathVariable Long id) {
        return obtenerLaberinto(id);
    }

    @PostMapping("/{id}/movimiento")
    public ResponseEntity<?> registrarMovimiento(@PathVariable Long id, @RequestBody MovimientoRequest request) {
        try {
            return ResponseEntity.ok(partidaService.registrarMovimiento(id, request.getNodoId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarPartida(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(partidaService.finalizarPartida(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/ruta-optima")
    public ResponseEntity<?> obtenerRutaOptima(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(partidaService.calcularRutaOptima(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
