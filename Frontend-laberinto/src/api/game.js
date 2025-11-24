import client from './client';

// Crear nueva partida
export const createPartidaRequest = (nivel) => 
    client.post('/partidas', { usuarioId: JSON.parse(localStorage.getItem('user')).id, nivel })
        .then(res => res.data);

// Obtener partida por ID
export const getPartidaRequest = (id) => 
    client.get(`/partidas/${id}`)
        .then(res => res.data);

// Obtener grafo del laberinto
export const getGrafoRequest = (partidaId) => 
    client.get(`/partidas/${partidaId}/laberinto`)
        .then(res => res.data);

// Registrar movimiento
export const sendMovementRequest = (partidaId, nodoId) => 
    client.post(`/partidas/${partidaId}/movimiento`, { nodoId })
        .then(res => res.data);

// Finalizar partida
export const finalizePartidaRequest = (partidaId) => 
    client.post(`/partidas/${partidaId}/finalizar`)
        .then(res => res.data);

// Obtener ruta óptima
export const getOptimalPathRequest = (partidaId) => 
    client.get(`/partidas/${partidaId}/ruta-optima`)
        .then(res => res.data);

// ========== NUEVAS FUNCIONES PARA RANKING Y RECOMPENSAS ==========

// Obtener ranking por nivel
export const getRankingByLevelRequest = (nivelId, limit = 10) => 
    client.get(`/ranking/nivel/${nivelId}?limit=${limit}`)
        .then(res => res.data);

// Obtener posición del usuario en un nivel
export const getUserPositionRequest = (usuarioId, nivelId) => 
    client.get(`/ranking/posicion/${usuarioId}/${nivelId}`)
        .then(res => res.data);

// Obtener todos los rankings del usuario
export const getUserRankingsRequest = (usuarioId) => 
    client.get(`/ranking/usuario/${usuarioId}`)
        .then(res => res.data);

// Obtener recompensas del usuario
export const getUserRewardsRequest = (usuarioId) => 
    client.get(`/ranking/recompensas/${usuarioId}`)
        .then(res => res.data);
