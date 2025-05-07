import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

class Node {
    Node parent;
    int[][] mat;
    int g, h;
    int x, y;

    public Node(int[][] mat, int x, int y, int g, int h, Node parent) {
        this.mat = new int[3][3];
        for (int i = 0; i < 3; i++)
            System.arraycopy(mat[i], 0, this.mat[i], 0, 3);
        this.g = g;
        this.h = h;
        this.x = x;
        this.y = y;
        this.parent = parent;
    }
}

public class ASTAR {

    static final int[] dRow = {1, 0, -1, 0};
    static final int[] dCol = {0, -1, 0, 1};
    static final int N = 3;

    static void printMatrix(int[][] mat, int g, int h) {
        for (int[] row : mat) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        System.out.println("g: " + g + " h: " + h + " f: " + (g + h));
    }

    static Node newNode(int[][] mat, int x, int y, int nx, int ny, int g, Node parent) {
        Node node = new Node(mat, nx, ny, g, Integer.MAX_VALUE, parent);
        int temp = node.mat[x][y];
        node.mat[x][y] = node.mat[nx][ny];
        node.mat[nx][ny] = temp;
        return node;
    }

    static int heuristic(int[][] initial, int[][] goal) {
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (initial[i][j] != 0 && initial[i][j] != goal[i][j])
                    count++;
            }
        }
        return count;
    }

    static boolean isSafe(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    static void printPath(Node root) {
        if (root == null) return;
        printPath(root.parent);
        printMatrix(root.mat, root.g, root.h);
        System.out.println();
    }

    static boolean isSolvable(int[][] start, int[][] goal) {
        int[] startArr = new int[N * N];
        int[] goalArr = new int[N * N];
        int idx = 0;
    
        // Flatten both matrices into 1D arrays
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                startArr[idx++] = start[i][j];
    
        idx = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                goalArr[idx++] = goal[i][j];
    
        // Map goal values to their positions
        int[] position = new int[N * N];
        for (int i = 0; i < N * N; i++) {
            position[goalArr[i]] = i;
        }
    
        // Convert start to goal-relative indices
        int[] relativeStart = new int[N * N];
        for (int i = 0; i < N * N; i++) {
            relativeStart[i] = position[startArr[i]];
        }
    
        // Count inversions in relativeStart (excluding 0)
        int invCount = 0;
        for (int i = 0; i < N * N; i++) {
            if (relativeStart[i] == 0) continue;
            for (int j = i + 1; j < N * N; j++) {
                if (relativeStart[j] == 0) continue;
                if (relativeStart[i] > relativeStart[j]) invCount++;
            }
        }
    
        return invCount % 2 == 0;
    }
    
    
    static void solve(int[][] start, int x, int y, int[][] goal) {
        PriorityQueue<Node> pq = new PriorityQueue<>((lhs, rhs) -> (lhs.g + lhs.h) - (rhs.g + rhs.h));
        Node root = newNode(start, x, y, x, y, 0, null);
        root.h = heuristic(start, goal);
        pq.add(root);

        while (!pq.isEmpty()) {
            Node m = pq.poll();
            if (m.h == 0) {
                System.out.println("\n\nThis puzzle is solved in " + m.g + " moves");
                printPath(m);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int dx = m.x + dRow[i];
                int dy = m.y + dCol[i];
                if (isSafe(dx, dy)) {
                    Node child = newNode(m.mat, m.x, m.y, dx, dy, m.g + 1, m);
                    child.h = heuristic(child.mat, goal);
                    pq.add(child);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] start = new int[N][N];
        int[][] goal = new int[N][N];
        int x = -1, y = -1;

        try (Scanner sc = new Scanner(new File("input2.txt"))) {
            System.out.println("Reading start and goal states from file 'input2.txt'...");

            // Reading start state
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    start[i][j] = sc.nextInt();
                    if (start[i][j] == 0) {
                        x = i;
                        y = j;
                    }
                }
            }

            // Reading goal state
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    goal[i][j] = sc.nextInt();
                }
            }

            if (!isSolvable(start, goal)) {
                System.out.println("This puzzle is unsolvable (odd number of inversions).");
            } else {
                solve(start, x, y, goal);
            }
            

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please make sure 'input.txt' exists in the directory.");
        }
    }
}

// input2.txt
// 2 8 3
// 1 6 4
// 7 0 5
// 1 2 3
// 8 0 4
// 7 6 5

// output:
// This puzzle is solved in 5 moves
// 2 8 3
// 1 6 4
// 7 0 5
// g: 0 h: 4 f: 4

// 2 8 3
// 1 0 4
// 7 6 5
// g: 1 h: 3 f: 4

// 2 0 3
// 1 8 4
// 7 6 5
// g: 2 h: 3 f: 5

// 0 2 3
// 1 8 4
// 7 6 5
// g: 3 h: 2 f: 5

// 1 2 3
// 0 8 4
// 7 6 5
// g: 4 h: 1 f: 5

// 1 2 3
// 8 0 4
// 7 6 5
// g: 5 h: 0 f: 5

