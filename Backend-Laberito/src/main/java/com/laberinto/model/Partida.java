package com.laberinto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    // @com.fasterxml.jackson.annotation.JsonIgnore // Descomentar si causa ciclos
    private Usuario usuario;

    @Column(nullable = false)
    private Integer nivel;

    @Column(name = "tiempo_inicio")
    private LocalDateTime tiempoInicio = LocalDateTime.now();

    @Column(name = "tiempo_fin")
    private LocalDateTime tiempoFin;

    private Integer pasos = 0;

    private Integer puntuacion = 0;

    @Enumerated(EnumType.STRING)
    private EstadoPartida estado = EstadoPartida.EN_CURSO;

    public enum EstadoPartida {
        EN_CURSO, COMPLETADA, ABANDONADA
    }
}
