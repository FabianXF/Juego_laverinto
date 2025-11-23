package com.laberinto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String nombre;
    private Long id;
    private String correo;

    @JsonProperty("nivel_actual")
    private Integer nivelActual;

    public AuthResponse(String token, String nombre, Long id, String correo, Integer nivelActual) {
        this.token = token;
        this.nombre = nombre;
        this.id = id;
        this.correo = correo;
        this.nivelActual = nivelActual;
    }
}
