# 📘 Guía Técnica: Backend "Escape del Laberinto"

Este documento explica cómo está construido el backend de tu juego, ideal para entender la lógica o para explicarlo en tu clase de **Matemáticas Discretas**.

---

## 🏗️ 1. Arquitectura del Proyecto
El sistema usa **Spring Boot (Java)** siguiendo el patrón de diseño **MVC (Modelo-Vista-Controlador)**, aunque aquí la "Vista" es una API REST que consume el Frontend.

### Capas Principales:
1.  **Controller (`/controller`)**: Son los "porteros". Reciben las peticiones HTTP (GET, POST) del frontend y deciden qué servicio llamar.
2.  **Service (`/service`)**: Es el "cerebro". Aquí está toda la lógica matemática (BFS, DFS) y las reglas del juego.
3.  **Repository (`/repository`)**: Es el "bibliotecario". Se encarga de hablar con la base de datos MySQL para guardar o leer datos.
4.  **Model (`/model`)**: Son los "planos". Definen cómo son los objetos (Usuario, Partida, Laberinto) y cómo se guardan en las tablas de la BD.

---

## 🧠 2. El Corazón Matemático (Grafos)

La parte más importante para tu materia está en `GrafoService.java`.

### A. Generación del Laberinto (DFS - Depth First Search)
Para crear un laberinto que **siempre tenga solución**, usamos un algoritmo llamado **Recursive Backtracker** (basado en DFS).
*   **¿Cómo funciona?**
    1.  Imagina el laberinto como una cuadrícula llena de paredes.
    2.  El algoritmo empieza en una celda aleatoria.
    3.  Busca un vecino que no haya visitado.
    4.  Rompe la pared entre ellos (crea una **arista** en el grafo).
    5.  Se mueve a ese vecino y repite.
    6.  Si se queda encerrado, "retrocede" (backtracking) hasta encontrar un camino nuevo.

### B. Solución Óptima (BFS - Breadth First Search)
Para calcular la ruta más corta y calificar al jugador, usamos **BFS**.
*   **¿Por qué BFS?** Porque en grafos no ponderados (o con pesos iguales), BFS garantiza encontrar el camino con menos aristas (pasos) desde el inicio hasta el final.
*   **¿Cómo funciona?**
    Explora el laberinto por "niveles" u "ondas". Primero mira todos los vecinos a 1 paso, luego a 2 pasos, etc., hasta encontrar la salida.

---

## 🔐 3. Seguridad (JWT)
No guardamos contraseñas en texto plano.
*   **Registro**: La contraseña se encripta usando **BCrypt** (se convierte en un hash ilegible).
*   **Login**: Si el usuario y contraseña coinciden, el servidor genera un **Token JWT**.
*   **Acceso**: Para jugar, el frontend debe enviar ese Token en cada petición. Si no lo envía, el backend rechaza la conexión (Error 403).

---

## 🗄️ 4. Base de Datos (MySQL)
Usamos **JPA (Java Persistence API)** para no escribir SQL a mano. Las tablas principales son:

*   **`usuarios`**: Jugadores registrados.
*   **`partidas`**: Cada intento de juego. Guarda el nivel, tiempo inicio/fin y puntuación.
*   **`laberintos`**: Guarda la estructura del laberinto generado.
    *   *Dato curioso*: Guardamos el grafo completo en formato JSON en la columna `graf_json` para poder reconstruirlo rápido sin hacer miles de consultas a la base de datos.
*   **`movimientos`**: Historial de cada paso que da el jugador (para auditoría o repeticiones).

---

## 🚀 5. Resumen de Endpoints (API)

| Método | URL | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | Crea un nuevo usuario. |
| `POST` | `/api/v1/auth/login` | Inicia sesión y devuelve el Token. |
| `POST` | `/api/v1/partidas` | Crea una partida nueva y **genera un laberinto único**. |
| `GET` | `/api/v1/partidas/{id}/laberinto` | Devuelve el laberinto (paredes y caminos) para dibujarlo. |
| `POST` | `/api/v1/partidas/{id}/movimiento` | Registra que el jugador se movió a una casilla. |
| `GET` | `/api/v1/partidas/{id}/ruta-optima` | **Ejecuta BFS** y devuelve la lista de pasos ideal. |

---

### ¿Listo para conectar el Frontend?
Ahora que entiendes cómo funciona el "cerebro", tu frontend solo necesita hacer llamadas a estas URLs para que el juego cobre vida.
