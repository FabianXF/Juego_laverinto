import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getRankingByLevelRequest, getUserPositionRequest } from '../api/game';
import { useAuth } from '../context/AuthContext';
import { Trophy, Medal, Award, ArrowLeft, Clock, Footprints } from 'lucide-react';
import { motion } from 'framer-motion';

const Ranking = () => {
    const { nivel } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();

    const [rankings, setRankings] = useState([]);
    const [userPosition, setUserPosition] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadRanking();
    }, [nivel]);

    const loadRanking = async () => {
        try {
            setLoading(true);

            // Cargar ranking del nivel (TOP 5)
            const rankingData = await getRankingByLevelRequest(parseInt(nivel), 5);
            setRankings(rankingData);

            // Cargar posición del usuario
            if (user) {
                const positionData = await getUserPositionRequest(user.id, parseInt(nivel));
                setUserPosition(positionData.posicion);
            }
        } catch (error) {
            console.error('Error cargando ranking:', error);
        } finally {
            setLoading(false);
        }
    };

    const getMedalIcon = (position) => {
        switch (position) {
            case 1:
                return <Trophy size={32} style={{ color: '#FFD700' }} />;
            case 2:
                return <Medal size={32} style={{ color: '#C0C0C0' }} />;
            case 3:
                return <Medal size={32} style={{ color: '#CD7F32' }} />;
            default:
                return <Award size={32} style={{ color: '#64748b' }} />;
        }
    };

    const formatTime = (seconds) => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    };

    if (loading) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
                <div className="glass-panel" style={{ padding: '20px' }}>Cargando ranking...</div>
            </div>
        );
    }

    return (
        <div style={{ padding: '40px 20px', maxWidth: '1200px', margin: '0 auto' }}>
            {/* Header */}
            <div style={{ marginBottom: '40px', textAlign: 'center' }}>
                <button
                    onClick={() => navigate('/levels')}
                    className="btn-secondary"
                    style={{ position: 'absolute', left: '20px', top: '20px' }}
                >
                    <ArrowLeft size={20} /> Volver
                </button>

                <h1 style={{ fontSize: '3rem', marginBottom: '10px' }}>
                    🏆 Top 5 - Nivel {nivel}
                </h1>

                {userPosition && (
                    <p style={{ fontSize: '1.2rem', color: 'var(--text-secondary)' }}>
                        Tu mejor posición: <span style={{ color: 'var(--primary)', fontWeight: 'bold' }}>#{userPosition}</span>
                    </p>
                )}
            </div>

            {/* Tabla de ranking */}
            <div className="glass-panel" style={{ padding: '0', overflow: 'hidden' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr style={{ background: 'rgba(255, 255, 255, 0.05)', borderBottom: '2px solid rgba(255, 255, 255, 0.1)' }}>
                            <th style={{ padding: '20px', textAlign: 'center', width: '80px' }}>Pos</th>
                            <th style={{ padding: '20px', textAlign: 'left' }}>Jugador</th>
                            <th style={{ padding: '20px', textAlign: 'center' }}>
                                <Clock size={20} style={{ verticalAlign: 'middle' }} /> Tiempo
                            </th>
                            <th style={{ padding: '20px', textAlign: 'center' }}>
                                <Footprints size={20} style={{ verticalAlign: 'middle' }} /> Pasos
                            </th>
                            <th style={{ padding: '20px', textAlign: 'center' }}>Puntuación</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rankings.length === 0 ? (
                            <tr>
                                <td colSpan="5" style={{ padding: '40px', textAlign: 'center', color: 'var(--text-secondary)' }}>
                                    No hay registros aún. ¡Sé el primero!
                                </td>
                            </tr>
                        ) : (
                            rankings.map((rank, index) => (
                                <motion.tr
                                    key={`${rank.id}-${index}`}
                                    initial={{ opacity: 0, x: -20 }}
                                    animate={{ opacity: 1, x: 0 }}
                                    transition={{ delay: index * 0.05 }}
                                    style={{
                                        borderBottom: '1px solid rgba(255, 255, 255, 0.05)',
                                        background: rank.usuarioId === user?.id ? 'rgba(59, 130, 246, 0.1)' : 'transparent',
                                        transition: 'background 0.3s'
                                    }}
                                    whileHover={{ background: 'rgba(255, 255, 255, 0.05)' }}
                                >
                                    <td style={{ padding: '20px', textAlign: 'center' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '10px' }}>
                                            {getMedalIcon(rank.posicion)}
                                            <span style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>
                                                #{rank.posicion}
                                            </span>
                                        </div>
                                    </td>
                                    <td style={{ padding: '20px' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                                            <div style={{
                                                width: '40px',
                                                height: '40px',
                                                borderRadius: '50%',
                                                background: 'linear-gradient(135deg, var(--primary), var(--accent))',
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center',
                                                fontSize: '1.2rem',
                                                fontWeight: 'bold'
                                            }}>
                                                {rank.nombreUsuario.charAt(0).toUpperCase()}
                                            </div>
                                            <span style={{ fontSize: '1.1rem', fontWeight: rank.usuarioId === user?.id ? 'bold' : 'normal' }}>
                                                {rank.nombreUsuario}
                                                {rank.usuarioId === user?.id && (
                                                    <span style={{ marginLeft: '10px', color: 'var(--primary)' }}>(Tú)</span>
                                                )}
                                            </span>
                                        </div>
                                    </td>
                                    <td style={{ padding: '20px', textAlign: 'center', fontFamily: 'var(--font-mono)', fontSize: '1.1rem' }}>
                                        {formatTime(rank.mejorTiempo)}
                                    </td>
                                    <td style={{ padding: '20px', textAlign: 'center', fontFamily: 'var(--font-mono)', fontSize: '1.1rem' }}>
                                        {rank.pasosUsados}
                                    </td>
                                    <td style={{ padding: '20px', textAlign: 'center' }}>
                                        <span style={{
                                            fontSize: '1.2rem',
                                            fontWeight: 'bold',
                                            color: 'var(--warning)',
                                            textShadow: '0 0 10px rgba(251, 191, 36, 0.5)'
                                        }}>
                                            {rank.puntuacion}
                                        </span>
                                    </td>
                                </motion.tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* Botón para jugar */}
            <div style={{ marginTop: '40px', textAlign: 'center' }}>
                <button
                    onClick={() => navigate(`/game/${nivel}`)}
                    className="btn-primary"
                    style={{ padding: '15px 40px', fontSize: '1.2rem' }}
                >
                    Jugar Nivel {nivel}
                </button>
            </div>
        </div>
    );
};

export default Ranking;
