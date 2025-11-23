# 🎮 Escape del Laberinto - Juego de Grafos

Un juego educativo que demuestra conceptos de **Matemáticas Discretas** y **Teoría de Grafos** mediante la resolución de laberintos generados algorítmicamente.

![Versión](https://img.shields.io/badge/versión-1.0.0-blue)
![Java](https://img.shields.io/badge/Java-17+-orange)
![React](https://img.shields.io/badge/React-18+-61DAFB)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-green)

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Conceptos de Matemáticas Discretas](#-conceptos-de-matemáticas-discretas)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Configuración y Ejecución](#-configuración-y-ejecución)
- [Estructura del Código](#-estructura-del-código)
- [Algoritmos Implementados](#-algoritmos-implementados)
- [API Endpoints](#-api-endpoints)
- [Capturas de Pantalla](#-capturas-de-pantalla)

---

## 🎯 Descripción General

**Escape del Laberinto** es una aplicación web full-stack que permite a los usuarios:

- ✅ Registrarse y autenticarse con JWT
- ✅ Jugar laberintos de diferentes niveles de dificultad
- ✅ Ver la ruta óptima calculada con BFS (Breadth-First Search)
- ✅ Competir por el mejor puntaje basado en tiempo y pasos
- ✅ Progresar a través de 5 niveles incrementales

El juego es una demostración práctica de cómo los **grafos** y los **algoritmos de búsqueda** se aplican en problemas del mundo real.

---

## 📐 Conceptos de Matemáticas Discretas

### 1. **Teoría de Grafos**

El laberinto se representa internamente como un **grafo no dirigido** donde:

- **Vértices (V)**: Cada celda del laberinto es un nodo
- **Aristas (E)**: Las conexiones entre celdas adyacentes sin paredes
- **Grafo G = (V, E)**: Estructura completa del laberinto

```
Ejemplo de representación:
Laberinto 3x3 = 9 nodos
Nodo 0 conectado a [1, 3]
Nodo 1 conectado a [0, 2, 4]
...
```

### 2. **Algoritmo DFS (Depth-First Search)**

Usado para **generar** el laberinto:

- **Propósito**: Crear un laberinto perfecto (un solo camino entre cualquier par de celdas)
- **Características**:
  - Garantiza conectividad completa
  - Genera laberintos con caminos largos y sinuosos
  - Complejidad: O(V + E)

**Pseudocódigo:**
```
DFS(nodo_actual, visitados):
    marcar nodo_actual como visitado
    para cada vecino no visitado:
        eliminar pared entre nodo_actual y vecino
        DFS(vecino, visitados)
```

### 3. **Algoritmo BFS (Breadth-First Search)**

Usado para **encontrar la ruta óptima**:

- **Propósito**: Calcular el camino más corto desde el inicio hasta la salida
- **Características**:
  - Garantiza la ruta con menor número de pasos
  - Explora nivel por nivel
  - Complejidad: O(V + E)

**Pseudocódigo:**
```
BFS(inicio, fin):
    cola = [inicio]
    visitados = {inicio}
    padres = {}
    
    mientras cola no esté vacía:
        nodo = cola.dequeue()
        si nodo == fin:
            reconstruir_camino(padres, fin)
        
        para cada vecino de nodo:
            si vecino no visitado:
                visitados.add(vecino)
                padres[vecino] = nodo
                cola.enqueue(vecino)
```

### 4. **Árboles de Expansión**

El laberinto generado es un **árbol de expansión** del grafo completo:

- **Árbol**: Grafo conexo sin ciclos
- **Expansión**: Incluye todos los vértices
- **Propiedades**:
  - |E| = |V| - 1 (para un laberinto de n×m: aristas = n×m - 1)
  - Exactamente un camino entre cualquier par de nodos

### 5. **Representación de Grafos**

El proyecto utiliza **lista de adyacencia** para almacenar el grafo:

```json
{
  "0": [1, 3],
  "1": [0, 2],
  "2": [1, 5],
  ...
}
```

**Ventajas:**
- Espacio: O(V + E)
- Eficiente para grafos dispersos
- Fácil iteración sobre vecinos

### 6. **Caminos y Conectividad**

- **Camino**: Secuencia de vértices donde cada par consecutivo está conectado
- **Camino Simple**: No repite vértices
- **Conectividad**: El grafo es conexo (existe camino entre cualquier par de nodos)
- **Distancia**: Longitud del camino más corto (calculada con BFS)

### 7. **Complejidad Computacional**

| Operación | Complejidad | Descripción |
|-----------|-------------|-------------|
| Generar laberinto (DFS) | O(n²) | n×n celdas |
| Encontrar ruta óptima (BFS) | O(n²) | Explorar todas las celdas |
| Verificar movimiento | O(1) | Consulta en lista de adyacencia |
| Almacenar grafo | O(n²) | Lista de adyacencia |

---

## 🏗️ Arquitectura del Proyecto

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE (React)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │   Login/     │  │   Selector   │  │     Juego    │ │
│  │   Registro   │  │  de Niveles  │  │  Laberinto   │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                          │ HTTP/REST
                          ▼
┌─────────────────────────────────────────────────────────┐
│              SERVIDOR (Spring Boot)                     │
│  ┌──────────────────────────────────────────────────┐  │
│  │           Controladores REST                     │  │
│  │  AuthController │ PartidaController              │  │
│  └──────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────┐  │
│  │              Servicios                           │  │
│  │  AuthService │ PartidaService │ LaberintoService│  │
│  │              │ GrafoService                      │  │
│  └──────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────┐  │
│  │           Repositorios JPA                       │  │
│  │  UsuarioRepo │ PartidaRepo │ LaberintoRepo      │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                          │ JDBC
                          ▼
┌─────────────────────────────────────────────────────────┐
│                  BASE DE DATOS (MySQL)                  │
│  Usuarios │ Partidas │ Laberintos │ Nodos │ Aristas    │
└─────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17+**: Lenguaje de programación
- **Spring Boot 3.2.3**: Framework principal
- **Spring Security + JWT**: Autenticación y autorización
- **Spring Data JPA**: ORM para base de datos
- **MySQL**: Base de datos relacional
- **Maven**: Gestión de dependencias

### Frontend
- **React 18**: Biblioteca de UI
- **React Router**: Navegación
- **Axios**: Cliente HTTP
- **Framer Motion**: Animaciones
- **Lucide React**: Iconos
- **Vite**: Build tool y dev server

---

## ⚙️ Configuración y Ejecución

### Prerrequisitos

```bash
# Verificar instalaciones
java -version    # Java 17 o superior
node -v          # Node.js 16 o superior
mysql -V         # MySQL 8.0 o superior
```

### 1. Configurar Base de Datos

**Opción A: Usar el script SQL incluido (Recomendado)**

```bash
# Ejecutar el script SQL
mysql -u root -p < database.sql

# O desde MySQL Workbench:
# 1. Abrir MySQL Workbench
# 2. File > Open SQL Script
# 3. Seleccionar database.sql
# 4. Ejecutar (⚡ icono de rayo)
```

**Opción B: Crear manualmente**

```sql
-- Crear base de datos
CREATE DATABASE laberinto_db;

-- Crear usuario (opcional)
CREATE USER 'laberinto_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON laberinto_db.* TO 'laberinto_user'@'localhost';
FLUSH PRIVILEGES;
```

> **Nota**: El archivo `database.sql` en la raíz del proyecto contiene el esquema completo de la base de datos con todas las tablas, índices y un usuario administrador de prueba.

### 2. Configurar Backend

```bash
# Navegar al directorio del backend
cd Backend-Laberito

# Editar application.properties si es necesario
# src/main/resources/application.properties

# Las tablas se crean automáticamente con JPA
```

**Archivo `application.properties`:**
```properties
spring.application.name=backend-laberinto
server.port=8081

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/laberinto_db
spring.datasource.username=root
spring.datasource.password=root

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT
jwt.secret=tu_clave_secreta_muy_segura_aqui
jwt.expiration=86400000
```

### 3. Ejecutar Backend

**Opción A: Usando el script (Windows)**
```bash
powershell -ExecutionPolicy Bypass -File "run_backend.ps1"
```

**Opción B: Usando Maven directamente**
```bash
.\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run
```

**Opción C: Desde IDE (IntelliJ/Eclipse)**
- Abrir el proyecto
- Ejecutar `BackendLaberintoApplication.java`

El backend estará disponible en: `http://localhost:8081`

### 4. Configurar Frontend

```bash
# Navegar al directorio del frontend
cd Frontend-laberinto

# Instalar dependencias (solo la primera vez)
npm install
```

### 5. Ejecutar Frontend

```bash
# Modo desarrollo
npm run dev

# El frontend estará en: http://localhost:5173
```

### 6. Acceder a la Aplicación

1. Abrir navegador en `http://localhost:5173`
2. Registrarse con un nuevo usuario
3. Iniciar sesión
4. ¡Jugar!

---

## 📁 Estructura del Código

### Backend (`Backend-Laberito/`)

```
Backend-Laberito/
├── src/main/java/com/laberinto/
│   ├── config/
│   │   ├── CorsConfig.java              # Configuración CORS
│   │   ├── JwtAuthenticationFilter.java # Filtro JWT
│   │   └── SecurityConfig.java          # Configuración de seguridad
│   ├── controller/
│   │   ├── AuthController.java          # Login/Registro
│   │   └── PartidaController.java       # CRUD de partidas
│   ├── dto/
│   │   ├── AuthResponse.java            # Respuesta de autenticación
│   │   ├── LoginRequest.java            # Datos de login
│   │   ├── MovimientoRequest.java       # Registro de movimiento
│   │   ├── PartidaRequest.java          # Crear partida
│   │   └── RegisterRequest.java         # Registro de usuario
│   ├── model/
│   │   ├── Arista.java                  # Conexión entre nodos
│   │   ├── Laberinto.java               # Entidad laberinto
│   │   ├── Movimiento.java              # Historial de movimientos
│   │   ├── Nodo.java                    # Celda del laberinto
│   │   ├── Partida.java                 # Sesión de juego
│   │   └── Usuario.java                 # Usuario del sistema
│   ├── repository/
│   │   ├── AristaRepository.java
│   │   ├── LaberintoRepository.java
│   │   ├── MovimientoRepository.java
│   │   ├── NodoRepository.java
│   │   ├── PartidaRepository.java
│   │   └── UsuarioRepository.java
│   ├── service/
│   │   ├── AuthService.java             # Lógica de autenticación
│   │   ├── GrafoService.java            # Algoritmos DFS/BFS
│   │   ├── LaberintoService.java        # Generación de laberintos
│   │   └── PartidaService.java          # Lógica de partidas
│   ├── util/
│   │   └── JwtUtil.java                 # Utilidades JWT
│   └── BackendLaberintoApplication.java # Clase principal
├── src/main/resources/
│   └── application.properties           # Configuración
└── pom.xml                              # Dependencias Maven
```

### Frontend (`Frontend-laberinto/`)

```
Frontend-laberinto/
├── src/
│   ├── api/
│   │   ├── auth.js                      # API de autenticación
│   │   ├── client.js                    # Cliente Axios
│   │   └── game.js                      # API del juego
│   ├── components/
│   │   └── MazeGrid.jsx                 # Componente del laberinto
│   ├── context/
│   │   └── AuthContext.jsx              # Contexto de autenticación
│   ├── pages/
│   │   ├── Game.jsx                     # Página del juego
│   │   ├── LevelSelector.jsx            # Selector de niveles
│   │   ├── Login.jsx                    # Página de login
│   │   └── Register.jsx                 # Página de registro
│   ├── App.jsx                          # Componente raíz
│   ├── index.css                        # Estilos globales
│   └── main.jsx                         # Punto de entrada
├── index.html
├── package.json                         # Dependencias npm
└── vite.config.js                       # Configuración Vite
```

---

## 🧮 Algoritmos Implementados

### 1. Generación de Laberinto (DFS)

**Archivo**: `GrafoService.java`

```java
public Map<Integer, List<Integer>> generarLaberintoDFS(int filas, int columnas) {
    int totalNodos = filas * columnas;
    Map<Integer, List<Integer>> adj = new HashMap<>();
    boolean[] visitado = new boolean[totalNodos];
    
    // Inicializar grafo vacío
    for (int i = 0; i < totalNodos; i++) {
        adj.put(i, new ArrayList<>());
    }
    
    // DFS desde nodo 0
    dfs(0, filas, columnas, visitado, adj, new Random());
    
    return adj;
}
```

**Características**:
- Garantiza un laberinto perfecto (sin ciclos)
- Todos los nodos son alcanzables
- Genera caminos largos y desafiantes

### 2. Búsqueda de Ruta Óptima (BFS)

**Archivo**: `GrafoService.java`

```java
public List<Integer> bfs(Map<Integer, List<Integer>> grafo, int inicio, int fin) {
    Queue<Integer> cola = new LinkedList<>();
    Map<Integer, Integer> padres = new HashMap<>();
    Set<Integer> visitados = new HashSet<>();
    
    cola.offer(inicio);
    visitados.add(inicio);
    padres.put(inicio, null);
    
    while (!cola.isEmpty()) {
        int actual = cola.poll();
        
        if (actual == fin) {
            return reconstruirCamino(padres, fin);
        }
        
        for (int vecino : grafo.getOrDefault(actual, new ArrayList<>())) {
            if (!visitados.contains(vecino)) {
                visitados.add(vecino);
                padres.put(vecino, actual);
                cola.offer(vecino);
            }
        }
    }
    
    return new ArrayList<>();
}
```

**Características**:
- Encuentra el camino más corto
- Complejidad O(V + E)
- Usado para mostrar la solución óptima

### 3. Cálculo de Puntuación

**Archivo**: `PartidaService.java`

```java
private int calcularPuntuacion(Partida partida, int pasosOptimos) {
    int pasos = partida.getMovimientos().size();
    long tiempoSegundos = Duration.between(
        partida.getFechaInicio(),
        partida.getFechaFin()
    ).getSeconds();
    
    // Penalización por pasos extra
    int penalizacionPasos = Math.max(0, (pasos - pasosOptimos) * 10);
    
    // Penalización por tiempo
    int penalizacionTiempo = (int) (tiempoSegundos * 2);
    
    // Puntuación base - penalizaciones
    return Math.max(0, 1000 - penalizacionPasos - penalizacionTiempo);
}
```

---

## 🔌 API Endpoints

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Registrar nuevo usuario |
| POST | `/api/v1/auth/login` | Iniciar sesión |

**Ejemplo de registro:**
```json
POST /api/v1/auth/register
{
  "nombre": "Juan Pérez",
  "correo": "juan@example.com",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "nombre": "Juan Pérez",
  "id": 1,
  "correo": "juan@example.com",
  "nivel_actual": 1
}
```

### Partidas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/partidas` | Crear nueva partida |
| GET | `/api/v1/partidas/{id}` | Obtener detalles de partida |
| GET | `/api/v1/partidas/{id}/laberinto` | Obtener grafo del laberinto |
| POST | `/api/v1/partidas/{id}/movimiento` | Registrar movimiento |
| POST | `/api/v1/partidas/{id}/finalizar` | Finalizar partida |
| GET | `/api/v1/partidas/{id}/ruta-optima` | Obtener ruta óptima (BFS) |

**Ejemplo de crear partida:**
```json
POST /api/v1/partidas
{
  "usuarioId": 1,
  "nivel": 1
}
```

**Respuesta:**
```json
{
  "id": 1,
  "nivel": 1,
  "completada": false,
  "puntuacion": 0,
  "fechaInicio": "2025-11-23T17:00:00"
}
```

---

## 📸 Capturas de Pantalla

### Pantalla de Login
![Login](docs/login.png)

### Selector de Niveles
![Niveles](docs/niveles.png)

### Juego en Acción
![Juego](docs/juego.png)

### Ruta Óptima (BFS)
![Ruta Óptima](docs/ruta-optima.png)

---

## 🎓 Aplicación Educativa

Este proyecto demuestra los siguientes conceptos de **Matemáticas Discretas**:

1. **Grafos**: Representación y manipulación
2. **Árboles**: Árboles de expansión
3. **Algoritmos de Búsqueda**: DFS y BFS
4. **Caminos**: Caminos simples y óptimos
5. **Conectividad**: Grafos conexos
6. **Complejidad**: Análisis de algoritmos
7. **Estructuras de Datos**: Listas, colas, conjuntos

---

## 👨‍💻 Autor

**Fabián XF**
- GitHub: [@FabianXF](https://github.com/FabianXF)
- Proyecto: Matemáticas Discretas - 7º Semestre

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 🙏 Agradecimientos

- Profesor de Matemáticas Discretas
- Comunidad de Spring Boot
- Comunidad de React

---

**¡Disfruta resolviendo laberintos con grafos!** 🎮🔢
