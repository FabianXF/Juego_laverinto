package com.laberinto.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "aristas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "laberinto_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Laberinto laberinto;

    @ManyToOne
    @JoinColumn(name = "nodo_a", nullable = false)
    // @com.fasterxml.j
    // ackson.annotation.JsonIgnore
    private Nodo nodoA;

    @ManyToOne
    @JoinColumn(name = "nodo_b", nullable = false)
    // @com.fasterxml.jackson.annotation.JsonIgnore
    private Nodo nodoB;

    private Integer peso = 1;
}
