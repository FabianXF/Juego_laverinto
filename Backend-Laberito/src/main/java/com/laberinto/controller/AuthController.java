package com.laberinto.controller;

import com.laberinto.dto.AuthResponse;
import com.laberinto.dto.LoginRequest;
import com.laberinto.dto.RegisterRequest;
import com.laberinto.model.Usuario;
import com.laberinto.service.AuthService;
import com.laberinto.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = authService.registrarUsuario(request.getNombre(), request.getCorreo(),
                    request.getPassword());
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = authService.login(request.getCorreo(), request.getPassword());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = jwtUtil.generateToken(usuario.getCorreo());
            return ResponseEntity.ok(new AuthResponse(token, usuario.getNombre(), usuario.getId(), usuario.getCorreo(),
                    usuario.getNivelActual()));
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
