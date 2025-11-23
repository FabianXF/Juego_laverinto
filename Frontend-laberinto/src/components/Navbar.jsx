import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, User, Map } from 'lucide-react';

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="glass-panel" style={{ margin: '20px', padding: '15px 30px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Link to="/" style={{ textDecoration: 'none', color: 'var(--primary)', fontSize: '1.5rem', fontWeight: 'bold', fontFamily: 'var(--font-mono)' }}>
                ESCAPE<span style={{ color: 'var(--text-primary)' }}>LABERINTO</span>
            </Link>

            <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
                {user ? (
                    <>
                        <Link to="/levels" className="btn-secondary" style={{ display: 'flex', alignItems: 'center', gap: '8px', textDecoration: 'none' }}>
                            <Map size={18} /> Niveles
                        </Link>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--text-secondary)' }}>
                            <User size={18} />
                            <span>{user.name}</span>
                        </div>
                        <button onClick={handleLogout} className="btn-secondary" style={{ border: '1px solid var(--danger)', color: 'var(--danger)' }}>
                            <LogOut size={18} />
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login" className="btn-secondary" style={{ textDecoration: 'none' }}>Iniciar Sesión</Link>
                        <Link to="/register" className="btn-primary" style={{ textDecoration: 'none' }}>Registrarse</Link>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Navbar;
