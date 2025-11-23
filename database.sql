-- ============================================
-- SCRIPT DE BASE DE DATOS - ESCAPE DEL LABERINTO
-- ============================================
-- Este script crea la base de datos y todas las tablas necesarias
-- para el juego "Escape del Laberinto"
-- ============================================

-- CREAR BASE DE DATOS
CREATE DATABASE IF NOT EXISTS laberinto_db;
USE laberinto_db;

-- ============================================
-- TABLA DE USUARIOS
-- ============================================
-- Almacena la información de los jugadores registrados
CREATE TABLE usuarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(150) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  rol ENUM('USER','ADMIN') DEFAULT 'USER',
  nivel_actual INT DEFAULT 1,
  fecha_registro DATETIME DEFAULT NOW()
);

-- ============================================
-- TABLA DE PARTIDAS
-- ============================================
-- Registra cada sesión de juego de un usuario
CREATE TABLE partidas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  nivel INT NOT NULL,
  tiempo_inicio DATETIME DEFAULT NOW(),
  tiempo_fin DATETIME NULL,
  pasos INT DEFAULT 0,
  puntuacion INT DEFAULT 0,
  estado ENUM('EN_CURSO','COMPLETADA','ABANDONADA') DEFAULT 'EN_CURSO',
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ============================================
-- TABLA DE LABERINTOS
-- ============================================
-- Almacena la estructura del laberinto generado para cada partida
-- graf_json contiene la representación del grafo como lista de adyacencia
CREATE TABLE laberintos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  partida_id BIGINT NULL,
  nivel INT NOT NULL,
  filas INT NOT NULL,
  columnas INT NOT NULL,
  graf_json TEXT,
  creado_en DATETIME DEFAULT NOW(),
  FOREIGN KEY (partida_id) REFERENCES partidas(id)
);

-- ============================================
-- TABLA DE NODOS
-- ============================================
-- Representa cada celda del laberinto
CREATE TABLE nodos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  laberinto_id BIGINT NOT NULL,
  fila INT NOT NULL,
  columna INT NOT NULL,
  es_inicio BOOLEAN DEFAULT FALSE,
  es_salida BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (laberinto_id) REFERENCES laberintos(id)
);

-- ============================================
-- TABLA DE ARISTAS
-- ============================================
-- Representa las conexiones entre nodos (celdas adyacentes sin pared)
CREATE TABLE aristas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  laberinto_id BIGINT NOT NULL,
  nodo_a BIGINT NOT NULL,
  nodo_b BIGINT NOT NULL,
  peso INT DEFAULT 1,
  FOREIGN KEY (laberinto_id) REFERENCES laberintos(id)
);

-- ============================================
-- TABLA DE MOVIMIENTOS
-- ============================================
-- Registra cada movimiento del jugador durante una partida
CREATE TABLE movimientos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  partida_id BIGINT NOT NULL,
  nodo_id BIGINT NOT NULL,
  momento DATETIME DEFAULT NOW(),
  orden INT NOT NULL,
  FOREIGN KEY (partida_id) REFERENCES partidas(id),
  FOREIGN KEY (nodo_id) REFERENCES nodos(id)
);

-- ============================================
-- DATOS INICIALES (OPCIONAL)
-- ============================================
-- Crear un usuario administrador de prueba
-- NOTA: En producción, la contraseña debe estar hasheada con BCrypt
-- Esta es solo para pruebas locales
INSERT INTO usuarios (nombre, correo, password_hash, rol, nivel_actual)
VALUES ('Administrador', 'admin@laberinto.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 1);

-- ============================================
-- ÍNDICES PARA MEJORAR RENDIMIENTO
-- ============================================
CREATE INDEX idx_partidas_usuario ON partidas(usuario_id);
CREATE INDEX idx_laberintos_partida ON laberintos(partida_id);
CREATE INDEX idx_nodos_laberinto ON nodos(laberinto_id);
CREATE INDEX idx_aristas_laberinto ON aristas(laberinto_id);
CREATE INDEX idx_movimientos_partida ON movimientos(partida_id);

-- ============================================
-- VERIFICACIÓN
-- ============================================
-- Mostrar las tablas creadas
SHOW TABLES;

-- Verificar usuario admin creado
SELECT id, nombre, correo, rol, nivel_actual FROM usuarios WHERE rol = 'ADMIN';
