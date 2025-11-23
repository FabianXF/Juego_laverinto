import { Link } from 'react-router-dom';
import { Play } from 'lucide-react';

const Home = () => {
    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '80vh', textAlign: 'center' }}>
            <h1 style={{ fontSize: '4rem', marginBottom: '20px', textShadow: '0 0 30px rgba(56, 189, 248, 0.5)' }}>
                ESCAPE DEL <span className="text-gradient">LABERINTO</span>
            </h1>
            <p style={{ fontSize: '1.2rem', color: 'var(--text-secondary)', maxWidth: '600px', marginBottom: '40px' }}>
                Desafía tu mente con laberintos generados por grafos. Encuentra la ruta óptima y compite contra el algoritmo BFS.
            </p>
            <Link to="/levels" className="btn-primary" style={{ fontSize: '1.2rem', padding: '15px 40px', display: 'flex', alignItems: 'center', gap: '10px', textDecoration: 'none' }}>
                <Play size={24} /> JUGAR AHORA
            </Link>
        </div>
    );
};

export default Home;
