package com.laberinto.service;

import com.laberinto.model.*;
import com.laberinto.repository.LaberintoRepository;
import com.laberinto.repository.MovimientoRepository;
import com.laberinto.repository.NodoRepository;
import com.laberinto.repository.PartidaRepository;
import com.laberinto.repository.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LaberintoService laberintoService;

    @Autowired
    private NodoRepository nodoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private GrafoService grafoService;

    @Autowired
    private LaberintoRepository laberintoRepository;

    @Transactional
    public Partida crearPartida(Long usuarioId, int nivel) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Partida partida = new Partida();
        partida.setUsuario(usuario);
        partida.setNivel(nivel);
        partida = partidaRepository.save(partida);

        laberintoService.generarLaberinto(partida, nivel);

        return partida;
    }

    public Partida obtenerPartida(Long partidaId) {
        return partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));
    }

    @Transactional
    public Movimiento registrarMovimiento(Long partidaId, Long nodoId) {
        Partida partida = obtenerPartida(partidaId);
        if (partida.getEstado() != Partida.EstadoPartida.EN_CURSO) {
            throw new RuntimeException("La partida no está en curso");
        }

        Nodo nodo = nodoRepository.findById(nodoId)
                .orElseThrow(() -> new RuntimeException("Nodo no encontrado"));

        int orden = partida.getPasos() + 1;

        Movimiento movimiento = new Movimiento();
        movimiento.setPartida(partida);
        movimiento.setNodo(nodo);
        movimiento.setOrden(orden);

        partida.setPasos(orden);
        partidaRepository.save(partida);

        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public Partida finalizarPartida(Long partidaId) {
        Partida partida = obtenerPartida(partidaId);
        partida.setTiempoFin(LocalDateTime.now());
        partida.setEstado(Partida.EstadoPartida.COMPLETADA);

        // Calcular puntuación
        // 1. Obtener ruta óptima (BFS)
        List<Integer> rutaOptima = calcularRutaOptima(partidaId);
        int pasosOptimos = rutaOptima.size() - 1; // Restamos 1 porque cuenta nodos, no pasos
        if (pasosOptimos < 0)
            pasosOptimos = 0;

        int pasosJugador = partida.getPasos();

        // 2. Calcular tiempo en segundos
        long segundos = java.time.temporal.ChronoUnit.SECONDS.between(partida.getTiempoInicio(),
                partida.getTiempoFin());

        // 3. Fórmula: Base (1000) - (PasosExtra * 10) - (Segundos * 2)
        int base = 1000;
        int pasosExtra = Math.max(0, pasosJugador - pasosOptimos);
        int penalizacionPasos = pasosExtra * 10;
        int penalizacionTiempo = (int) segundos * 2;

        int puntuacionFinal = Math.max(0, base - penalizacionPasos - penalizacionTiempo);

        partida.setPuntuacion(puntuacionFinal);

        // Actualizar nivel del usuario si completó su nivel actual
        Usuario usuario = partida.getUsuario();
        if (usuario.getNivelActual() == partida.getNivel()) {
            usuario.setNivelActual(usuario.getNivelActual() + 1);
            usuarioRepository.save(usuario);
        }

        return partidaRepository.save(partida);
    }

    public List<Integer> calcularRutaOptima(Long partidaId) {
        Partida partida = obtenerPartida(partidaId);
        Laberinto laberinto = laberintoRepository.findByPartidaId(partidaId)
                .orElseThrow(() -> new RuntimeException("Laberinto no encontrado"));

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Integer>> adjStringKeys = mapper.readValue(laberinto.getGrafJson(), Map.class);

            Map<Integer, List<Integer>> adj = new HashMap<>();
            for (Map.Entry<String, List<Integer>> entry : adjStringKeys.entrySet()) {
                adj.put(Integer.parseInt(entry.getKey()), entry.getValue());
            }

            int start = 0;
            int goal = (laberinto.getFilas() * laberinto.getColumnas()) - 1;

            return grafoService.bfs(adj, start, goal);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar el grafo del laberinto", e);
        }
    }
}
