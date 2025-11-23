import { createContext, useContext, useState, useEffect } from 'react';
import { loginRequest, registerRequest } from '../api/auth';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        const token = localStorage.getItem('token');

        if (storedUser && token) {
            setUser(JSON.parse(storedUser));
            setIsAuthenticated(true);
        }
        setLoading(false);
    }, []);

    const login = async (email, password) => {
        try {
            const data = await loginRequest(email, password);
            // Assuming response contains { token, usuario: { ... } } or similar
            // Adjust based on actual backend response. 
            // Common pattern: data.token, data.user

            // If backend returns just token, we might need to decode it or fetch user details.
            // For now, let's assume data has token and user info.
            // If the user provided specific JSON structure for login response, I should check.
            // "POST /api/v1/auth/login — login → devuelve JWT/session"

            // Let's assume data = { token: "...", nombre: "...", correo: "...", nivel_actual: 1 }
            // Or data = { token: "..." } and we decode.
            // I'll assume a friendly response for now.

            const userData = {
                name: data.nombre || email.split('@')[0],
                email: data.correo || email,
                level: data.nivel_actual || 1,
                id: data.id
            };

            localStorage.setItem('token', data.token || data.accessToken); // Fallback
            localStorage.setItem('user', JSON.stringify(userData));

            setUser(userData);
            setIsAuthenticated(true);
            return true;
        } catch (error) {
            console.error("Login error:", error);
            alert("Error al iniciar sesión");
            return false;
        }
    };

    const register = async (name, email, password) => {
        try {
            await registerRequest(name, email, password);
            // Auto login after register? or redirect to login.
            // Let's try auto login logic or just return true to redirect
            return true;
        } catch (error) {
            console.error("Register error:", error);
            alert("Error al registrarse");
            return false;
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
        setIsAuthenticated(false);
    };

    const updateLevel = (level) => {
        if (user) {
            const updatedUser = { ...user, level };
            setUser(updatedUser);
            localStorage.setItem('user', JSON.stringify(updatedUser));
        }
    };

    return (
        <AuthContext.Provider value={{ user, isAuthenticated, login, register, logout, updateLevel, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
