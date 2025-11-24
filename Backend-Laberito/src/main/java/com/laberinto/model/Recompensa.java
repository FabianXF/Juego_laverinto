package com.laberinto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recompensas")
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 50)
    private String tipo; // gold_medal, silver_medal, bronze_medal, speed_master, perfect_run

    @Column(nullable = false)
    private Integer nivel;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "fecha_obtencion")
    private LocalDateTime fechaObtencion;

    // Constructors
    public Recompensa() {
        this.fechaObtencion = LocalDateTime.now();
    }

    public Recompensa(Usuario usuario, String tipo, Integer nivel, String descripcion) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.fechaObtencion = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
