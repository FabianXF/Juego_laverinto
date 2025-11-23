import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';

const CELL_SIZE = 40; // px

const MazeGrid = ({ levelId, mazeData, optimalPathData, onFinish, onStep }) => {
    const [grid, setGrid] = useState([]);
    const [playerPos, setPlayerPos] = useState({ r: 0, c: 0 });
    const [showPath, setShowPath] = useState(false);
    const [dimensions, setDimensions] = useState({ rows: 0, cols: 0 });

    // Transform graf_json (Adjacency List) to Grid with Walls
    useEffect(() => {
        if (!mazeData) return;

        console.log("🔍 MazeData recibido:", mazeData);
        console.log("🔍 Tipo de mazeData:", typeof mazeData);
        console.log("🔍 Primer nodo (0):", mazeData[0]);
        console.log("🔍 Segundo nodo (1):", mazeData[1]);

        // Calculate dimensions from the maze data
        const totalNodes = Object.keys(mazeData).length;
        const cols = Math.sqrt(totalNodes);
        const rows = cols;

        console.log("🔍 Dimensiones calculadas:", { rows, cols, totalNodes });
        setDimensions({ rows, cols });

        const newGrid = [];
        for (let r = 0; r < rows; r++) {
            const row = [];
            for (let c = 0; c < cols; c++) {
                const nodeId = r * cols + c;
                const neighbors = mazeData[nodeId] || []; // Array of neighbor IDs

                // Debug first cell
                if (nodeId === 0) {
                    console.log("🔍 Nodo 0 vecinos:", neighbors);
                }

                // Determine walls based on neighbors
                // If neighbor is NOT in list, there is a wall
                const topNeighbor = (r - 1) * cols + c;
                const bottomNeighbor = (r + 1) * cols + c;
                const leftNeighbor = r * cols + (c - 1);
                const rightNeighbor = r * cols + (c + 1);

                row.push({
                    r, c,
                    id: nodeId,
                    walls: {
                        top: !neighbors.includes(topNeighbor),
                        bottom: !neighbors.includes(bottomNeighbor),
                        left: !neighbors.includes(leftNeighbor),
                        right: !neighbors.includes(rightNeighbor)
                    }
                });
            }
            newGrid.push(row);
        }
        setGrid(newGrid);
        setPlayerPos({ r: 0, c: 0 });
    }, [mazeData]);

    useEffect(() => {
        const handleKeyDown = (e) => {
            if (grid.length === 0) return;

            const { r, c } = playerPos;
            const currentCell = grid[r][c];
            let nextPos = null;

            // Prevent default scrolling
            if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(e.key)) {
                e.preventDefault();
            }

            if (e.key === 'ArrowUp' && !currentCell.walls.top) nextPos = { r: r - 1, c };
            if (e.key === 'ArrowDown' && !currentCell.walls.bottom) nextPos = { r: r + 1, c };
            if (e.key === 'ArrowLeft' && !currentCell.walls.left) nextPos = { r, c: c - 1 };
            if (e.key === 'ArrowRight' && !currentCell.walls.right) nextPos = { r, c: c + 1 };

            if (nextPos) {
                setPlayerPos(nextPos);
                const nextNodeId = nextPos.r * dimensions.cols + nextPos.c;
                onStep(nextNodeId); // Send node ID to parent

                // Check win
                if (nextPos.r === dimensions.rows - 1 && nextPos.c === dimensions.cols - 1) {
                    onFinish();
                }
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [playerPos, grid, dimensions]);

    if (grid.length === 0) return <div>Cargando Laberinto...</div>;

    // Helper to check if a cell is in the optimal path
    const isInOptimalPath = (r, c) => {
        if (!optimalPathData) return false;
        const nodeId = r * dimensions.cols + c;
        return optimalPathData.includes(nodeId);
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', maxWidth: '100%' }}>
            <div
                style={{
                    display: 'grid',
                    gridTemplateColumns: `repeat(${dimensions.cols}, ${CELL_SIZE}px)`,
                    gap: 0,
                    border: '2px solid var(--text-secondary)',
                    position: 'relative',
                    background: 'rgba(0,0,0,0.3)',
                    boxShadow: '0 0 30px rgba(0,0,0,0.5)',
                    touchAction: 'none'
                }}
            >
                {grid.map((row, r) => (
                    row.map((cell, c) => (
                        <div
                            key={`${r}-${c}`}
                            style={{
                                width: CELL_SIZE,
                                height: CELL_SIZE,
                                borderTop: cell.walls.top ? '2px solid var(--text-secondary)' : 'none',
                                borderRight: cell.walls.right ? '2px solid var(--text-secondary)' : 'none',
                                borderBottom: cell.walls.bottom ? '2px solid var(--text-secondary)' : 'none',
                                borderLeft: cell.walls.left ? '2px solid var(--text-secondary)' : 'none',
                                position: 'relative'
                            }}
                        >
                            {/* Start Marker */}
                            {r === 0 && c === 0 && <div style={{ position: 'absolute', inset: 0, background: 'rgba(34, 197, 94, 0.2)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '10px', color: 'var(--success)' }}>INICIO</div>}
                            {/* End Marker */}
                            {r === dimensions.rows - 1 && c === dimensions.cols - 1 && <div style={{ position: 'absolute', inset: 0, background: 'rgba(239, 68, 68, 0.2)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '10px', color: 'var(--danger)' }}>FIN</div>}

                            {/* Optimal Path Visualization */}
                            {showPath && isInOptimalPath(r, c) && (
                                <div style={{ position: 'absolute', inset: '12px', borderRadius: '50%', background: 'var(--primary)', boxShadow: '0 0 10px var(--primary)' }} />
                            )}
                        </div>
                    ))
                ))}

                {/* Player */}
                <motion.div
                    initial={false}
                    animate={{
                        top: playerPos.r * CELL_SIZE,
                        left: playerPos.c * CELL_SIZE
                    }}
                    transition={{ type: "spring", stiffness: 300, damping: 30 }}
                    style={{
                        position: 'absolute',
                        width: CELL_SIZE,
                        height: CELL_SIZE,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 10
                    }}
                >
                    <div style={{ width: '20px', height: '20px', borderRadius: '50%', background: 'var(--warning)', boxShadow: '0 0 15px var(--warning)' }} />
                </motion.div>
            </div>

            <button
                onClick={() => setShowPath(!showPath)}
                className="btn-secondary"
                style={{ marginTop: '20px' }}
            >
                {showPath ? 'Ocultar Ruta Óptima' : 'Mostrar Ruta Óptima (BFS)'}
            </button>
        </div>
    );
};

export default MazeGrid;
