package com.laberinto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "laberintos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laberinto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Partida partida;

    @Column(nullable = false)
    private Integer nivel;

    @Column(nullable = false)
    private Integer filas;

    @Column(nullable = false)
    private Integer columnas;

    @Column(name = "graf_json", columnDefinition = "TEXT")
    private String grafJson;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();
}
