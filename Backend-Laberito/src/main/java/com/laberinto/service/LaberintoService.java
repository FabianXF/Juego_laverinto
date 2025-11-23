package com.laberinto.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laberinto.model.Arista;
import com.laberinto.model.Laberinto;
import com.laberinto.model.Nodo;
import com.laberinto.model.Partida;
import com.laberinto.repository.AristaRepository;
import com.laberinto.repository.LaberintoRepository;
import com.laberinto.repository.NodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LaberintoService {

    @Autowired
    private GrafoService grafoService;

    @Autowired
    private LaberintoRepository laberintoRepository;

    @Autowired
    private NodoRepository nodoRepository;

    @Autowired
    private AristaRepository aristaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public Laberinto generarLaberinto(Partida partida, int nivel) {
        int filas = 0;
        int columnas = 0;

        switch (nivel) {
            case 1:
                filas = 12;
                columnas = 12;
                break;
            case 2:
                filas = 15;
                columnas = 15;
                break;
            case 3:
                filas = 18;
                columnas = 18;
                break;
            case 4:
                filas = 22;
                columnas = 22;
                break;
            case 5:
                filas = 25;
                columnas = 25;
                break;
            default:
                filas = 12;
                columnas = 12;
                break;
        }

        Map<Integer, List<Integer>> adj = grafoService.generarLaberintoDFS(filas, columnas);

        Laberinto laberinto = new Laberinto();
        laberinto.setPartida(partida);
        laberinto.setNivel(nivel);
        laberinto.setFilas(filas);
        laberinto.setColumnas(columnas);

        try {
            laberinto.setGrafJson(objectMapper.writeValueAsString(adj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        laberinto = laberintoRepository.save(laberinto);

        // Crear Nodos
        List<Nodo> nodos = new ArrayList<>();
        for (int i = 0; i < filas * columnas; i++) {
            Nodo nodo = new Nodo();
            nodo.setLaberinto(laberinto);
            nodo.setFila(i / columnas);
            nodo.setColumna(i % columnas);
            if (i == 0)
                nodo.setEsInicio(true);
            if (i == (filas * columnas) - 1)
                nodo.setEsSalida(true);
            nodos.add(nodo);
        }
        nodoRepository.saveAll(nodos);

        // Crear Aristas
        List<Arista> aristas = new ArrayList<>();
        // Para evitar duplicados en aristas no dirigidas, solo agregamos si u < v
        for (Map.Entry<Integer, List<Integer>> entry : adj.entrySet()) {
            int u = entry.getKey();
            for (int v : entry.getValue()) {
                if (u < v) {
                    Arista arista = new Arista();
                    arista.setLaberinto(laberinto);
                    // Buscar nodos correspondientes (optimización: usar mapa temporal o buscar en
                    // lista)
                    // Como nodos están ordenados por ID (0..N-1), podemos acceder por índice
                    arista.setNodoA(nodos.get(u));
                    arista.setNodoB(nodos.get(v));
                    aristas.add(arista);
                }
            }
        }
        aristaRepository.saveAll(aristas);

        return laberinto;
    }
}
