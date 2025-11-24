package com.laberinto.dto;

import java.time.LocalDateTime;

public class RankingDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private Integer nivel;
    private Integer mejorTiempo;
    private Integer pasosUsados;
    private Integer puntuacion;
    private LocalDateTime fechaLogro;
    private Long posicion; // Posición en el ranking

    // Constructors
    public RankingDTO() {
    }

    public RankingDTO(Long id, Long usuarioId, String nombreUsuario, Integer nivel,
            Integer mejorTiempo, Integer pasosUsados, Integer puntuacion,
            LocalDateTime fechaLogro) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.nivel = nivel;
        this.mejorTiempo = mejorTiempo;
        this.pasosUsados = pasosUsados;
        this.puntuacion = puntuacion;
        this.fechaLogro = fechaLogro;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getMejorTiempo() {
        return mejorTiempo;
    }

    public void setMejorTiempo(Integer mejorTiempo) {
        this.mejorTiempo = mejorTiempo;
    }

    public Integer getPasosUsados() {
        return pasosUsados;
    }

    public void setPasosUsados(Integer pasosUsados) {
        this.pasosUsados = pasosUsados;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public LocalDateTime getFechaLogro() {
        return fechaLogro;
    }

    public void setFechaLogro(LocalDateTime fechaLogro) {
        this.fechaLogro = fechaLogro;
    }

    public Long getPosicion() {
        return posicion;
    }

    public void setPosicion(Long posicion) {
        this.posicion = posicion;
    }
}
