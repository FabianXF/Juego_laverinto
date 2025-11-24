package com.laberinto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer nivel;

    @Column(name = "mejor_tiempo", nullable = false)
    private Integer mejorTiempo; // en segundos

    @Column(name = "pasos_usados", nullable = false)
    private Integer pasosUsados;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(name = "fecha_logro")
    private LocalDateTime fechaLogro;

    // Constructors
    public Ranking() {
        this.fechaLogro = LocalDateTime.now();
    }

    public Ranking(Usuario usuario, Integer nivel, Integer mejorTiempo, Integer pasosUsados, Integer puntuacion) {
        this.usuario = usuario;
        this.nivel = nivel;
        this.mejorTiempo = mejorTiempo;
        this.pasosUsados = pasosUsados;
        this.puntuacion = puntuacion;
        this.fechaLogro = LocalDateTime.now();
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
}
