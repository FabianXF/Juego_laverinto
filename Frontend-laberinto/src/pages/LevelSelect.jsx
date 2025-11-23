import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Lock, Unlock } from 'lucide-react';

const levels = [
    { id: 1, size: '6x6', difficulty: 'Muy Fácil', nodes: 36 },
    { id: 2, size: '8x8', difficulty: 'Fácil', nodes: 64 },
    { id: 3, size: '10x10', difficulty: 'Media', nodes: 100 },
    { id: 4, size: '12x12', difficulty: 'Difícil', nodes: 144 },
    { id: 5, size: '15x15', difficulty: 'Muy Difícil', nodes: 225 },
];

const LevelSelect = () => {
    const { user } = useAuth();

    return (
        <div style={{ padding: '20px' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '40px', fontSize: '2.5rem' }}>Selecciona un Nivel</h2>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '30px' }}>
                {levels.map((level) => {
                    const isLocked = user.level < level.id;
                    return (
                        <div key={level.id} className="glass-panel" style={{ padding: '30px', position: 'relative', opacity: isLocked ? 0.6 : 1, transition: 'transform 0.3s ease' }}>
                            <div style={{ position: 'absolute', top: '20px', right: '20px', color: isLocked ? 'var(--text-secondary)' : 'var(--success)' }}>
                                {isLocked ? <Lock /> : <Unlock />}
                            </div>
                            <h3 style={{ fontSize: '2rem', marginBottom: '10px', color: 'var(--primary)' }}>Nivel {level.id}</h3>
                            <div style={{ marginBottom: '20px', color: 'var(--text-secondary)' }}>
                                <p>Tamaño: <span style={{ color: 'var(--text-primary)' }}>{level.size}</span></p>
                                <p>Nodos: <span style={{ color: 'var(--text-primary)' }}>{level.nodes}</span></p>
                                <p>Dificultad: <span style={{ color: isLocked ? 'var(--text-secondary)' : 'var(--warning)' }}>{level.difficulty}</span></p>
                            </div>

                            {isLocked ? (
                                <button className="btn-secondary" disabled style={{ width: '100%', cursor: 'not-allowed' }}>Bloqueado</button>
                            ) : (
                                <Link to={`/game/${level.id}`} className="btn-primary" style={{ display: 'block', textAlign: 'center', textDecoration: 'none' }}>
                                    Jugar
                                </Link>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default LevelSelect;
