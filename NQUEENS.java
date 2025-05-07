import java.util.*; // Importing Java utility classes

public class NQUEENS

{
    // Function to print the chess board with placed queens
    static void printBoard(int board[][], int N)
    {
        for(int row[]: board) // For each row in the board
        {
            for(int cell: row) // For each cell in the row
            {
                System.out.print(cell == 1 ? "Q " : ". "); // Print 'Q' if queen is placed, else '.'
            }
            System.out.println(); // Move to next line after printing a row
        }
    }

    // Function to check if a queen can be safely placed at board[row][col]
    static boolean isSafe(int board[][], int row, int col, int N)
    {
        // Check if there is any queen in the same column above the current row
        for(int i = 0; i < row; i++)
        {
            if(board[i][col] == 1)
            {
                return false;
            }
        }

        // Check the upper left diagonal for queens
        for(int i = row, j = col; i >= 0 && j >= 0; i--, j--)
        {
            if(board[i][j] == 1)
            {
                return false;
            }
        }

        // Check the upper right diagonal for queens
        for(int i = row, j = col; i >= 0 && j < N; i--, j++)
        {
            if(board[i][j] == 1)
            {
                return false;
            }
        }

        return true; // Safe to place the queen
    }

    // Solves N-Queens problem using backtracking
    static boolean solveByBacktracking(int board[][], int row, int N)
    {
        // If all queens are placed, return true
        if(row == N)
            return true;
        
        // Try placing a queen in all columns of the current row
        for(int col = 0; col < N; col++)
        {
            // Check if it's safe to place a queen here
            if(isSafe(board, row, col, N))
            {
                board[row][col] = 1; // Place the queen
                System.out.println("Placing queen at (" + row + "," + col + "):");
                printBoard(board, N); // Print the board after placing queen

                // Recurse to place rest of the queens
                if(solveByBacktracking(board, row + 1, N))
                    return true;

                // If placing queen at board[row][col] doesn't lead to a solution,
                // then backtrack (remove queen and try next position)
                board[row][col] = 0;
                System.out.println("Backtracking from (" + row + "," + col + "):");
                printBoard(board, N); // Print the board after backtracking
            }
        }

        return false; // No valid position found in this row
    }

    // Solves N-Queens using Branch and Bound optimization
    static boolean solveByBranchAndBound(int[][] board, int row, int N, int colConflicts[], int diag1Conflicts[], int diag2Conflicts[])
    {
        // If all queens are placed, return true
        if(row == N)
            return true;

        // Try placing a queen in all columns of current row
        for(int col = 0; col < N; col++)
        {
            // Check if column and diagonals are free of conflicts
            if(colConflicts[col] == 0 && diag1Conflicts[row - col + N - 1] == 0 && diag2Conflicts[row + col] == 0)
            {
                board[row][col] = 1; // Place the queen
                colConflicts[col] = 1; // Mark column as occupied
                diag1Conflicts[row - col + N - 1] = 1; // Mark \ diagonal as occupied
                diag2Conflicts[row + col] = 1; // Mark / diagonal as occupied

                System.out.println("Placing queen at (" + row + "," + col + "):");
                printBoard(board, N); // Print the board after placing queen

                // Recurse to place rest of the queens
                if(solveByBranchAndBound(board, row + 1, N, colConflicts, diag1Conflicts, diag2Conflicts))
                    return true;

                // Backtrack and unmark the position and conflicts
                board[row][col] = 0;
                colConflicts[col] = 0;
                diag1Conflicts[row - col + N - 1] = 0;
                diag2Conflicts[row + col] = 0;

                System.out.println("Backtracking from (" + row + "," + col + "):");
                printBoard(board, N); // Print the board after backtracking
            }
        }

        return false; // No valid position found in this row
    }

    // Main function to take input and solve the problem
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in); // Scanner for user input

        System.out.println("Enter the number of queens:");
        int N = sc.nextInt(); // Number of queens (also size of the board)

        int choice = 0; // User's menu choice
        int board[][] = new int[N][N]; // Create N x N chess board

        // Menu-driven loop until user exits
        while(choice != 3)
        {
            // Display choices to user
            System.out.println("1.Solve by Backtracking\n2.Solve by Branch and Bound\n3.Exit\nEnter your choice: ");
            choice = sc.nextInt(); // Take user's choice

            switch (choice) 
            {
                case 1:
                    System.out.println("\nBacktracking Solution: ");
                    // Try solving with backtracking
                    if(solveByBacktracking(board, 0, N))
                    {
                        System.out.println("Solution found using Backtracking");
                        printBoard(board, N); // Print the final board
                    }
                    else 
                    {
                        System.out.println("No solution exists for " + N + " queens using Backtracking");
                    }
                    break;

                case 2:
                    System.out.println("\nBranch and Bound Solution: ");
                    // Arrays to mark conflicts in columns and diagonals
                    int[] colConflicts = new int[N];
                    int[] diag1Conflicts = new int[2 * N - 1];
                    int[] diag2Conflicts = new int[2 * N - 1];

                    // Try solving with branch and bound
                    if(solveByBranchAndBound(board, 0, N, colConflicts, diag1Conflicts, diag2Conflicts))
                    {
                        System.out.println("Solution found using Branch and Bound:");
                        printBoard(board, N); // Print the final board
                    }
                    else 
                    {
                        System.out.println("No solution exists for " + N + " queens using Branch and Bound");
                    }
                    break;

                case 3:
                    System.out.println("Exited from the program!"); // Exit message
                    break;

                default:
                    System.out.println("Invalid choice!"); // Handle invalid menu input
                    break;
            }
        }
    }
}

// output:
// Enter the number of queens:
// 4
// 1.Solve by Backtracking
// 2.Solve by Branch and Bound
// 3.Exit
// Enter your choice: 
// 1

// Backtracking Solution: 
// Placing queen at (0,0):
// Q . . . 
// . . . . 
// . . . . 
// . . . . 
// Placing queen at (1,2):
// Q . . . 
// . . Q . 
// . . . . 
// . . . . 
// Backtracking from (1,2):
// Q . . . 
// . . . .
// . . . .
// . . . .
// Placing queen at (1,3):
// Q . . .
// . . . Q
// . . . .
// . . . .
// Placing queen at (2,1):
// Q . . .
// . . . Q
// . Q . .
// . . . .
// Backtracking from (2,1):
// Q . . .
// . . . Q
// . . . .
// . . . .
// Backtracking from (1,3):
// Q . . .
// . . . .
// . . . .
// . . . .
// Backtracking from (0,0):
// . . . .
// . . . .
// . . . .
// . . . .
// Placing queen at (0,1):
// . Q . .
// . . . .
// . . . .
// . . . .
// Placing queen at (1,3):
// . Q . .
// . . . Q
// . . . .
// . . . .
// Placing queen at (2,0):
// . Q . .
// . . . Q
// Q . . .
// . . . .
// Placing queen at (3,2):
// . Q . .
// . . . Q
// Q . . .
// . . Q .
// Solution found using Backtracking
// . Q . .
// . . . Q
// Q . . .
// . . Q .
// 1.Solve by Backtracking
// 2.Solve by Branch and Bound
// 3.Exit
// Enter your choice:
// 2

// Branch and Bound Solution:
// Placing queen at (0,0):
// Q Q . .
// . . . Q
// Q . . .
// . . Q .
// Placing queen at (1,2):
// Q Q . .
// . . Q Q
// Q . . .
// . . Q .
// Backtracking from (1,2):
// Q Q . .
// . . . Q
// Q . . .
// . . Q .
// Placing queen at (1,3):
// Q Q . .
// . . . Q
// Q . . .
// . . Q .
// Placing queen at (2,1):
// Q Q . .
// . . . Q
// Q Q . .
// . . Q .
// Backtracking from (2,1):
// Q Q . .
// . . . Q
// Q . . .
// . . Q .
// Backtracking from (1,3):
// Q Q . .
// . . . .
// Q . . .
// . . Q .
// Backtracking from (0,0):
// . Q . .
// . . . .
// Q . . .
// . . Q .
// Placing queen at (0,1):
// . Q . .
// . . . .
// Q . . .
// . . Q .
// Placing queen at (1,3):
// . Q . .
// . . . Q
// Q . . .
// . . Q .
// Placing queen at (2,0):
// . Q . .
// . . . Q
// Q . . .
// . . Q .
// Placing queen at (3,2):
// . Q . .
// . . . Q
// Q . . .
// . . Q .
// Solution found using Branch and Bound:
// . Q . .
// . . . Q
// Q . . .
// . . Q .
// 1.Solve by Backtracking
// 2.Solve by Branch and Bound
// 3.Exit
// Enter your choice:
// 3
// Exited from the program!