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
-- TABLA DE RANKING
-- ============================================
-- Almacena TODOS los tiempos de cada jugador por nivel
-- Permite múltiples registros del mismo usuario en el mismo nivel
CREATE TABLE IF NOT EXISTS ranking (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  nivel INT NOT NULL,
  mejor_tiempo INT NOT NULL COMMENT 'Tiempo en segundos',
  pasos_usados INT NOT NULL COMMENT 'Número de pasos',
  puntuacion INT NOT NULL COMMENT 'Puntuación obtenida',
  fecha_logro DATETIME DEFAULT NOW() COMMENT 'Fecha del intento',
  
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  
  -- Índices para consultas rápidas
  INDEX idx_ranking_nivel (nivel),
  INDEX idx_ranking_usuario (usuario_id),
  INDEX idx_ranking_tiempo (mejor_tiempo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA DE RECOMPENSAS
-- ============================================
-- Almacena los logros y recompensas obtenidas por los jugadores
CREATE TABLE IF NOT EXISTS recompensas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  tipo VARCHAR(50) NOT NULL COMMENT 'Tipo: gold_medal, silver_medal, bronze_medal, speed_master, perfect_run',
  nivel INT NOT NULL COMMENT 'Nivel donde se obtuvo la recompensa',
  descripcion VARCHAR(255) COMMENT 'Descripción del logro',
  fecha_obtencion DATETIME DEFAULT NOW(),
  
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  
  -- Índices
  INDEX idx_usuario (usuario_id),
  INDEX idx_tipo (tipo),
  INDEX idx_nivel (nivel),
  INDEX idx_fecha (fecha_obtencion)
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

-- Índices específicos para ranking
CREATE INDEX idx_ranking_nivel_tiempo ON ranking(nivel, mejor_tiempo ASC);
CREATE INDEX idx_ranking_nivel_puntuacion ON ranking(nivel, puntuacion DESC);

-- ============================================
-- VERIFICACIÓN
-- ============================================
-- Mostrar las tablas creadas
SHOW TABLES;

-- Verificar usuario admin creado
SELECT id, nombre, correo, rol, nivel_actual FROM usuarios WHERE rol = 'ADMIN';
