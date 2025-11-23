export const generateMaze = (rows, cols) => {
    const maze = [];
    for (let r = 0; r < rows; r++) {
        const row = [];
        for (let c = 0; c < cols; c++) {
            row.push({
                r, c,
                walls: { top: true, right: true, bottom: true, left: true },
                visited: false
            });
        }
        maze.push(row);
    }

    const stack = [];
    const start = maze[0][0];
    start.visited = true;
    stack.push(start);

    while (stack.length > 0) {
        const current = stack[stack.length - 1];
        const neighbors = getUnvisitedNeighbors(current, maze, rows, cols);

        if (neighbors.length > 0) {
            const next = neighbors[Math.floor(Math.random() * neighbors.length)];
            removeWalls(current, next);
            next.visited = true;
            stack.push(next);
        } else {
            stack.pop();
        }
    }

    // Reset visited for gameplay
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            maze[r][c].visited = false;
        }
    }

    return maze;
};

const getUnvisitedNeighbors = (cell, maze, rows, cols) => {
    const neighbors = [];
    const { r, c } = cell;

    if (r > 0 && !maze[r - 1][c].visited) neighbors.push(maze[r - 1][c]);
    if (r < rows - 1 && !maze[r + 1][c].visited) neighbors.push(maze[r + 1][c]);
    if (c > 0 && !maze[r][c - 1].visited) neighbors.push(maze[r][c - 1]);
    if (c < cols - 1 && !maze[r][c + 1].visited) neighbors.push(maze[r][c + 1]);

    return neighbors;
};

const removeWalls = (a, b) => {
    const dr = a.r - b.r;
    const dc = a.c - b.c;

    if (dr === 1) { // a is below b
        a.walls.top = false;
        b.walls.bottom = false;
    } else if (dr === -1) { // a is above b
        a.walls.bottom = false;
        b.walls.top = false;
    } else if (dc === 1) { // a is right of b
        a.walls.left = false;
        b.walls.right = false;
    } else if (dc === -1) { // a is left of b
        a.walls.right = false;
        b.walls.left = false;
    }
};
