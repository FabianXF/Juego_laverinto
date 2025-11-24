import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import LevelSelect from './pages/LevelSelect';
import Game from './pages/Game';
import Ranking from './pages/Ranking';

// Protected Route Component
const ProtectedRoute = ({ children }) => {
    const { user } = useAuth();
    if (!user) return <Navigate to="/login" />;
    return children;
};

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Layout>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route
                            path="/levels"
                            element={
                                <ProtectedRoute>
                                    <LevelSelect />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/game/:id"
                            element={
                                <ProtectedRoute>
                                    <Game />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/ranking/:nivel"
                            element={
                                <ProtectedRoute>
                                    <Ranking />
                                </ProtectedRoute>
                            }
                        />
                    </Routes>
                </Layout>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
