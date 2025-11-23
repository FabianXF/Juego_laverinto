import client from './client';

export const createPartidaRequest = async (nivel) => {
    // Get user ID from storage (saved during login)
    const userStr = localStorage.getItem('user');
    let usuarioId = null;
    if (userStr) {
        const userObj = JSON.parse(userStr);
        usuarioId = userObj.id;
    }

    // Construct payload matching Backend expectation: { usuarioId: Long, nivel: int }
    const payload = {
        usuarioId: Number(usuarioId),
        nivel: Number(nivel)
    };

    const response = await client.post('/partidas', payload);
    return response.data;
};

export const getPartidaRequest = async (id) => {
    const response = await client.get(`/partidas/${id}`);
    return response.data;
};

export const getGrafoRequest = async (id) => {
    const response = await client.get(`/partidas/${id}/graf`);
    return response.data;
};

export const sendMovementRequest = async (partidaId, nodoId) => {
    const response = await client.post(`/partidas/${partidaId}/movimiento`, { nodoId });
    return response.data;
};

export const finalizePartidaRequest = async (partidaId) => {
    const response = await client.post(`/partidas/${partidaId}/finalizar`);
    return response.data;
};

export const getOptimalPathRequest = async (partidaId) => {
    const response = await client.get(`/partidas/${partidaId}/ruta-optima`);
    return response.data;
};
