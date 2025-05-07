//BFS-DFS - 1

import java.util.*;
import java.io.*;

class Graph {
    private Map<Integer, List<Integer>> adjList = new HashMap<>();

    public void addEdge(int u, int v) {
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
    }

    public void dfs(int start) {
        Set<Integer> visited = new HashSet<>();
        List<Integer> traversal = new ArrayList<>();
        System.out.println("\nDFS (Step-by-step traversal):");
        dfsRecursive(start, visited, 0, traversal);
    }

    public void dfsRecursive(int node, Set<Integer> visited, int depth, List<Integer> traversal) {
        if (visited.contains(node))
            return;

        visited.add(node);
        traversal.add(node);
        System.out.println("Visited Node: " + node + " at depth: " + depth + " -> Current DFS Traversal: " + traversal);

        for (int neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsRecursive(neighbor, visited, depth + 1, traversal);
            }
        }
    }

    public void bfs(int start) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> traversal = new ArrayList<>();

        visited.add(start);
        queue.add(start);

        int level = 0;
        System.out.println("\nBFS (Level-by-level, step-by-step traversal):");

        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.print("Level " + level + ": ");
            for (int i = 0; i < size; i++) {
                int node = queue.poll();
                traversal.add(node);
                System.out.print(node + " ");
                for (int neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            System.out.println("-> Current BFS Traversal: " + traversal);
            level++;
        }
    }
}

public class BFSDFS {
    public static void main(String[] args) {
        Graph g = new Graph();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of edges: ");
        int numEdges = sc.nextInt();

        System.out.println("Enter edges (src dest):");
        for (int i = 0; i < numEdges; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            g.addEdge(u, v);
        }

        System.out.print("Enter the starting node for traversal: ");
        int start = sc.nextInt();

        g.dfs(start);
        g.bfs(start);
    }
}
//Output:
// Enter the number of edges: 11
// Enter edges (src dest):
// 0 1
// 0 3
// 1 2
// 1 3
// 1 5
// 1 6
// 2 3
// 2 4
// 2 5
// 3 4
// 4 6
// Enter the starting node for traversal: 0

// DFS (Step-by-step traversal):
// Visited Node: 0 at depth: 0 -> Current DFS Traversal: [0]
// Visited Node: 1 at depth: 1 -> Current DFS Traversal: [0, 1]
// Visited Node: 2 at depth: 2 -> Current DFS Traversal: [0, 1, 2]
// Visited Node: 3 at depth: 3 -> Current DFS Traversal: [0, 1, 2, 3]
// Visited Node: 4 at depth: 4 -> Current DFS Traversal: [0, 1, 2, 3, 4]
// Visited Node: 6 at depth: 5 -> Current DFS Traversal: [0, 1, 2, 3, 4, 6]
// Visited Node: 5 at depth: 3 -> Current DFS Traversal: [0, 1, 2, 3, 4, 6, 5]

// BFS (Level-by-level, step-by-step traversal):
// Level 0: 0 -> Current BFS Traversal: [0]
// Level 1: 1 3 -> Current BFS Traversal: [0, 1, 3]
// Level 2: 2 5 6 4 -> Current BFS Traversal: [0, 1, 3, 2, 5, 6, 4]


