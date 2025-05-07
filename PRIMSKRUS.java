import java.util.*;

class Edge {
    int src, des, weight;

    Edge(int s, int d, int w) {
        src = s;
        des = d;
        weight = w;
    }

}

public class PRIMSKRUS {
    static final int INF = Integer.MAX_VALUE; // Constant representing infinity

    // Function to perform Prim's algorithm and print the Minimum Spanning Tree
    // (MST)
    public static void primsMST(int[][] graph, int V) {
        int[] key = new int[V]; // Key values used to pick minimum weight edge
        boolean[] mstSet = new boolean[V]; // Boolean array to represent set of vertices included in MST
        int[] parent = new int[V]; // Array to store constructed MST

        Arrays.fill(key, INF); // Initialize all key values to infinity
        Arrays.fill(mstSet, false); // Initially no vertices are included in MST
        key[0] = 0; // Start from the first vertex, so key of first vertex is 0
        parent[0] = -1; // First node is the root of MST, so it has no parent

        // Loop to construct MST with V vertices
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V); // Pick the vertex u with the minimum key value not yet included in MST
            mstSet[u] = true; // Include the picked vertex in the MST set

            // Update key and parent index of the adjacent vertices of the picked vertex
            for (int v = 0; v < V; v++) {
                // If graph[u][v] is non-zero, v is not in MST, and weight of (u,v) is smaller
                // than current key[v]
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u; // Update parent of v
                    key[v] = graph[u][v]; // Update key value
                }
            }
        }

        // Print the constructed MST
        System.out.println("\nPrim's MST:");
        int totalWeight = 0;
        for (int i = 1; i < V; i++) {
            System.out.println(parent[i] + " - " + i + " : " + graph[i][parent[i]]); // Print edge and its weight
            totalWeight += graph[i][parent[i]]; // Add edge weight to total MST weight
        }
        System.out.println("Total Weight: " + totalWeight); // Print total weight of MST
    }

    // Helper function to find the vertex with the minimum key value not yet
    // included in MST
    private static int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, minIndex = -1;

        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v]; // Update minimum
                minIndex = v; // Store index of minimum
            }
        }
        return minIndex; // Return the index of the vertex with the minimum key
    }

    // Function to perform Kruskal's algorithm and print the Minimum Spanning Tree
    // (MST)
    public static void kruskalMST(List<Edge> edges, int V) {
        // Sort all edges in non-decreasing order based on their weight
        Collections.sort(edges, Comparator.comparingInt(e -> e.weight));

        int parent[] = new int[V]; // Array to store parent of each vertex for union-find
        int rank[] = new int[V]; // Array to store rank of each subset for union by rank optimization

        // Initially, each vertex is its own parent (makeset)
        for (int i = 0; i < V; i++) {
            parent[i] = i;
        }

        List<Edge> result = new ArrayList<>(); // List to store edges included in MST
        int totalWeight = 0; // Total weight of MST

        // Traverse sorted edges and apply union-find
        for (Edge e : edges) {
            int uSet = findSet(e.src, parent); // Find set (root) of source vertex
            int vSet = findSet(e.des, parent); // Find set (root) of destination vertex

            // If including this edge does not form a cycle (i.e., sets are different)
            if (uSet != vSet) {
                result.add(e); // Include edge in MST
                totalWeight += e.weight; // Add edge weight to total MST weight
                unionSet(uSet, vSet, parent, rank); // Union the two sets
            }
        }

        // Print the constructed MST
        System.out.println("\nKruskal's MST: ");
        for (Edge e : result) {
            System.out.println(e.src + " - " + e.des + " :" + e.weight); // Print each edge
        }
        System.out.println("Total weight: " + totalWeight); // Print total weight of MST
    }

    // Function to find the representative (root) of the set that element u belongs
    // to
    private static int findSet(int u, int[] parent) {
        if (parent[u] != u) {
            parent[u] = findSet(parent[u], parent); // Path compression
        }
        return parent[u];
    }

    // Function to perform union of two subsets u and v using union by rank
    private static void unionSet(int u, int v, int[] parent, int[] rank) {
        u = findSet(u, parent); // Find root of set u
        v = findSet(v, parent); // Find root of set v

        // Attach smaller rank tree under root of higher rank tree
        if (rank[u] < rank[v]) {
            parent[u] = v;
        } else if (rank[u] > rank[v]) {
            parent[v] = u;
        } else {
            parent[v] = u;
            rank[u]++; // Increase rank if both trees have same rank
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of vertices and edges: ");
        int V = sc.nextInt();
        int E = sc.nextInt();

        int[][] adjMatrix = new int[V][V];
        List<Edge> edges = new ArrayList<>();

        System.out.println("Enter edges (src dest weight):");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();
            adjMatrix[u][v] = w;
            adjMatrix[v][u] = w;
            edges.add(new Edge(u, v, w));
        }

        primsMST(adjMatrix, V);
        kruskalMST(edges, V);
    }
}

// output:
// Enter number of vertices and edges: 5 7
// Enter edges (src dest weight):
// 0 1 4
// 0 2 4 
// 1 2 2
// 1 3 6
// 2 3 8
// 2 4 9
// 3 4 5

// Prim's MST:
// 0 - 1 : 4
// 1 - 2 : 2
// 1 - 3 : 6
// 3 - 4 : 5
// Total Weight: 17

// Kruskal's MST: 
// 1 - 2 :2
// 0 - 1 :4
// 3 - 4 :5
// 1 - 3 :6
// Total weight: 17
