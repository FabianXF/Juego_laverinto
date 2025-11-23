import client from './client';

export const loginRequest = async (email, password) => {
    const response = await client.post('/auth/login', { correo: email, password });
    return response.data;
};

export const registerRequest = async (nombre, email, password) => {
    const response = await client.post('/auth/register', { nombre, correo: email, password });
    return response.data;
};
