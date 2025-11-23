package com.laberinto.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GrafoService {

    // Representación del grafo: Map<Integer, List<Integer>> adjacency list
    // Integer es el ID del nodo (fila * columnas + columna)

    public Map<Integer, List<Integer>> generarLaberintoDFS(int filas, int columnas) {
        Map<Integer, List<Integer>> adj = new HashMap<>();
        boolean[][] visited = new boolean[filas][columnas];
        Stack<Integer> stack = new Stack<>();
        Random rand = new Random();

        // Inicializar grafo vacío (sin aristas)
        for (int i = 0; i < filas * columnas; i++) {
            adj.put(i, new ArrayList<>());
        }

        int startNode = 0;
        stack.push(startNode);
        visited[0][0] = true;

        while (!stack.isEmpty()) {
            int current = stack.peek();
            int r = current / columnas;
            int c = current % columnas;

            List<Integer> neighbors = getUnvisitedNeighbors(r, c, filas, columnas, visited);

            if (!neighbors.isEmpty()) {
                int next = neighbors.get(rand.nextInt(neighbors.size()));
                int nextR = next / columnas;
                int nextC = next % columnas;

                // Agregar arista (bidireccional)
                adj.get(current).add(next);
                adj.get(next).add(current);

                visited[nextR][nextC] = true;
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        return adj;
    }

    private List<Integer> getUnvisitedNeighbors(int r, int c, int filas, int columnas, boolean[][] visited) {
        List<Integer> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Arriba, Abajo, Izquierda, Derecha

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (nr >= 0 && nr < filas && nc >= 0 && nc < columnas && !visited[nr][nc]) {
                neighbors.add(nr * columnas + nc);
            }
        }
        return neighbors;
    }

    public List<Integer> bfs(Map<Integer, List<Integer>> adj, int start, int goal) {
        Queue<Integer> q = new LinkedList<>();
        Map<Integer, Integer> parent = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        q.add(start);
        visited.add(start);
        parent.put(start, null);

        while (!q.isEmpty()) {
            int u = q.poll();
            if (u == goal) break;

            for (int v : adj.getOrDefault(u, Collections.emptyList())) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    parent.put(v, u);
                    q.add(v);
                }
            }
        }

        if (!parent.containsKey(goal)) return Collections.emptyList(); // No hay camino

        // Reconstruir ruta
        LinkedList<Integer> path = new LinkedList<>();
        Integer cur = goal;
        while (cur != null) {
            path.addFirst(cur);
            cur = parent.get(cur);
        }
        return path;
    }
}
