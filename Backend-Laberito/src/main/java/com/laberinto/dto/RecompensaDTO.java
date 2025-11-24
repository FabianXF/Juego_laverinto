package com.laberinto.dto;

import java.time.LocalDateTime;

public class RecompensaDTO {
    private Long id;
    private String tipo;
    private Integer nivel;
    private String descripcion;
    private LocalDateTime fechaObtencion;

    // Constructors
    public RecompensaDTO() {
    }

    public RecompensaDTO(Long id, String tipo, Integer nivel, String descripcion, LocalDateTime fechaObtencion) {
        this.id = id;
        this.tipo = tipo;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.fechaObtencion = fechaObtencion;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(LocalDateTime fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }
}
