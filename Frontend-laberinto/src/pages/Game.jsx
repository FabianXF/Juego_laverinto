import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import MazeGrid from '../components/MazeGrid';
import { useAuth } from '../context/AuthContext';
import { createPartidaRequest, getOptimalPathRequest, finalizePartidaRequest, sendMovementRequest, getGrafoRequest } from '../api/game';
import { Clock, Footprints } from 'lucide-react';

const Game = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { updateLevel } = useAuth();

    const [partidaId, setPartidaId] = useState(null);
    const [mazeData, setMazeData] = useState(null);
    const [optimalPath, setOptimalPath] = useState(null);

    const [steps, setSteps] = useState(0);
    const [time, setTime] = useState(0);
    const [isFinished, setIsFinished] = useState(false);
    const [score, setScore] = useState(0);
    const [optimalStepsCount, setOptimalStepsCount] = useState(0);

    // Prevent double init
    const initialized = useRef(false);

    const initGame = async () => {
        setMazeData(null); // Show loading
        setSteps(0);
        setTime(0);
        setIsFinished(false);
        setScore(0);
        setPartidaId(null);
        setOptimalPath(null);
        setOptimalStepsCount(0);

        try {
            // 1. Create Partida
            const partida = await createPartidaRequest(parseInt(id));
            setPartidaId(partida.id);
            console.log("Partida creada:", partida);

            // 2. Fetch Maze Graph separately
            const graphResponse = await getGrafoRequest(partida.id);
            console.log("Grafo recibido:", graphResponse);

            let graph = graphResponse;
            if (graphResponse.grafJson) graph = graphResponse.grafJson;
            if (graphResponse.graf_json) graph = graphResponse.graf_json;

            if (typeof graph === 'string') {
                try {
                    graph = JSON.parse(graph);
                } catch (e) {
                    console.error("Error parsing graph JSON:", e);
                }
            }
            setMazeData(graph);

            // 3. Get Optimal Path for reference
            try {
                const path = await getOptimalPathRequest(partida.id);
                setOptimalPath(path);
                setOptimalStepsCount(path.length);
            } catch (e) {
                console.error("Error fetching optimal path", e);
            }

        } catch (error) {
            console.error("Error initializing game:", error);
            alert("Error al iniciar la partida. Asegúrate de que el backend esté corriendo.");
            navigate('/levels');
        }
    };

    useEffect(() => {
        if (initialized.current) return;
        initialized.current = true;
        initGame();
    }, [id]);

    useEffect(() => {
        if (!partidaId || isFinished) return;
        const timer = setInterval(() => {
            setTime(t => t + 1);
        }, 1000);
        return () => clearInterval(timer);
    }, [partidaId, isFinished]);

    const handleStep = async (nodeId) => {
        if (!isFinished) {
            setSteps(s => s + 1);
            // Send movement to backend (fire and forget to avoid lag)
            if (partidaId) {
                sendMovementRequest(partidaId, nodeId).catch(e => console.error(e));
            }
        }
    };

    const handleFinish = async () => {
        setIsFinished(true);

        try {
            const result = await finalizePartidaRequest(partidaId);
            // Result should contain score calculated by backend
            setScore(result.puntuacion || 0);

            // Update user level if passed
            updateLevel(parseInt(id) + 1);
        } catch (error) {
            console.error("Error finalizing:", error);
            // Fallback local score calculation if backend fails
            const stepPenalty = Math.max(0, (steps - (optimalStepsCount - 1)) * 10);
            const timePenalty = time * 2;
            setScore(Math.max(0, 1000 - stepPenalty - timePenalty));
        }
    };

    const handleRetry = () => {
        initGame();
    };

    if (!mazeData) return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
            <div className="glass-panel" style={{ padding: '20px' }}>Cargando partida desde el servidor...</div>
        </div>
    );

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '20px' }}>
            <div className="glass-panel" style={{ padding: '15px 30px', marginBottom: '20px', display: 'flex', gap: '40px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <Clock className="text-accent" />
                    <span style={{ fontSize: '1.5rem', fontFamily: 'var(--font-mono)' }}>{new Date(time * 1000).toISOString().substr(14, 5)}</span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <Footprints className="text-accent" />
                    <span style={{ fontSize: '1.5rem', fontFamily: 'var(--font-mono)' }}>{steps}</span>
                </div>
            </div>

            <MazeGrid
                levelId={id}
                mazeData={mazeData}
                optimalPathData={optimalPath}
                onFinish={handleFinish}
                onStep={handleStep}
            />

            {isFinished && (
                <div style={{
                    position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100
                }}>
                    <div className="glass-panel" style={{ padding: '40px', textAlign: 'center', maxWidth: '400px', width: '90%' }}>
                        <h2 style={{ fontSize: '3rem', color: 'var(--success)', marginBottom: '20px' }}>¡Nivel Completado!</h2>
                        <div style={{ marginBottom: '30px', textAlign: 'left' }}>
                            <p style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                <span>Tiempo:</span>
                                <span className="font-mono">{time}s</span>
                            </p>
                            <p style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                <span>Pasos:</span>
                                <span className="font-mono">{steps}</span>
                            </p>
                            <p style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px', color: 'var(--primary)' }}>
                                <span>Óptimo (BFS):</span>
                                <span className="font-mono">{Math.max(0, optimalStepsCount - 1)}</span>
                            </p>
                            <hr style={{ margin: '20px 0', borderColor: 'rgba(255,255,255,0.1)' }} />
                            <p style={{ fontSize: '1.5rem', fontWeight: 'bold', display: 'flex', justifyContent: 'space-between' }}>
                                <span>Puntuación:</span>
                                <span style={{ color: 'var(--warning)' }}>{score}</span>
                            </p>
                        </div>
                        <div style={{ display: 'flex', gap: '20px', justifyContent: 'center' }}>
                            <button onClick={() => navigate('/levels')} className="btn-secondary">Volver</button>
                            <button onClick={handleRetry} className="btn-primary">Reintentar</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Game;
