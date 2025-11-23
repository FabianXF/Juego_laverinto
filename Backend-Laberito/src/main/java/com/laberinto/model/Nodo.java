package com.laberinto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "nodos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "laberinto_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Laberinto laberinto;

    @Column(nullable = false)
    private Integer fila;

    @Column(nullable = false)
    private Integer columna;

    @Column(name = "es_inicio")
    private Boolean esInicio = false;

    @Column(name = "es_salida")
    private Boolean esSalida = false;
}
