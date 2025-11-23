# Backend Laberinto

Este es el backend para el juego "Escape del Laberinto", desarrollado con Spring Boot.

## Requisitos

- Java 17
- Maven
- MySQL

## Configuración

1.  Crea una base de datos en MySQL llamada `laberinto_db`.
2.  Configura tus credenciales de base de datos en `src/main/resources/application.properties`.

## Ejecución

```bash
mvn spring-boot:run
```

## API Endpoints

### Auth
- `POST /api/v1/auth/register`: Registrar usuario
- `POST /api/v1/auth/login`: Iniciar sesión

### Partidas
- `POST /api/v1/partidas`: Crear partida (Body: `{ "usuarioId": 1, "nivel": 1 }`)
- `GET /api/v1/partidas/{id}`: Obtener estado de partida
- `GET /api/v1/partidas/{id}/laberinto`: Obtener laberinto
- `POST /api/v1/partidas/{id}/movimiento`: Registrar movimiento (Body: `{ "nodoId": 5 }`)
- `POST /api/v1/partidas/{id}/finalizar`: Finalizar partida
- `GET /api/v1/partidas/{id}/ruta-optima`: Obtener ruta óptima (BFS)

## Estructura del Proyecto

- `com.laberinto.controller`: Controladores REST
- `com.laberinto.service`: Lógica de negocio y algoritmos (BFS/DFS)
- `com.laberinto.model`: Entidades JPA
- `com.laberinto.repository`: Repositorios de datos
- `com.laberinto.config`: Configuración de seguridad y JWT
