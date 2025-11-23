export const solveMazeBFS = (maze, startNode, endNode) => {
    const rows = maze.length;
    const cols = maze[0].length;
    const queue = [[startNode]];
    const visited = new Set();
    visited.add(`${startNode.r},${startNode.c}`);

    while (queue.length > 0) {
        const path = queue.shift();
        const current = path[path.length - 1];

        if (current.r === endNode.r && current.c === endNode.c) {
            return path;
        }

        const neighbors = getAccessibleNeighbors(current, maze, rows, cols);
        for (const neighbor of neighbors) {
            if (!visited.has(`${neighbor.r},${neighbor.c}`)) {
                visited.add(`${neighbor.r},${neighbor.c}`);
                queue.push([...path, neighbor]);
            }
        }
    }
    return [];
};

const getAccessibleNeighbors = (cell, maze, rows, cols) => {
    const neighbors = [];
    const { r, c, walls } = cell;

    // Check walls to see if we can move
    if (!walls.top && r > 0) neighbors.push(maze[r - 1][c]);
    if (!walls.bottom && r < rows - 1) neighbors.push(maze[r + 1][c]);
    if (!walls.left && c > 0) neighbors.push(maze[r][c - 1]);
    if (!walls.right && c < cols - 1) neighbors.push(maze[r][c + 1]);

    return neighbors;
};
