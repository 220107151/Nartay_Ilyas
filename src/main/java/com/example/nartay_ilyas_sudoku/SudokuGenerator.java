package com.example.nartay_ilyas_sudoku;

import java.util.Random;



public class SudokuGenerator {
    private static final String[] NUMBERS = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final int SIZE = 9;
    private static final int BLOCK_SIZE = 3;
    private static final String EMPTY_CELL = "";
    private static final int EASY_LEVEL = 20; // Number of cells to remove for an easy puzzle
    private static final int MEDIUM_LEVEL = 30; // Number of cells to remove for a medium puzzle
    private static final int HARD_LEVEL = 50; // Number of cells to remove for a hard puzzle
    private static final int MAX_TRIES = 100; // Maximum number of attempts to generate a valid puzzle
    private   static String[][] solution = new String[SIZE][SIZE];
    public static String[][] generate(int level) {
        if (level == 1) {
            return generatePuzzle(EASY_LEVEL);
        } else if (level == 2) {
            return generatePuzzle(MEDIUM_LEVEL);
        } else if (level == 3) {
            return generatePuzzle(HARD_LEVEL);
        } else {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    private static String[][] generatePuzzle(int numCellsToRemove) {

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                solution[row][col] = EMPTY_CELL;
            }
        }
        Random random = new Random();
        int numTries = 0;
        while (numTries < MAX_TRIES) {
            boolean success = generateSolution(solution, random);
            if (success) {
                String[][] puzzle = copyBoard(solution);
                removeCells(puzzle, numCellsToRemove, random);
                return puzzle;
            } else {
                numTries++;
            }
        }
        throw new RuntimeException("Failed to generate a valid puzzle");
    }

    private static boolean generateSolution(String[][] board, Random random) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col].equals(EMPTY_CELL)) {
                    for (int num = 1; num <= SIZE; num++) {
                        String numStr = Integer.toString(num);
                        if (isValid(board, row, col, numStr)) {
                            board[row][col] = numStr;
                            if (generateSolution(board, random)) {
                                return true;
                            } else {
                                board[row][col] = EMPTY_CELL;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static void removeCells(String[][] board, int numCellsToRemove, Random random) {
        int cellsRemoved = 0;
        while (cellsRemoved < numCellsToRemove) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (!board[row][col].equals(EMPTY_CELL)) {
                String temp = board[row][col];
                board[row][col] = EMPTY_CELL;
                int numSolutions = countSolutions(board, random);
                if (numSolutions != 1) {
                    board[row][col] = temp;
                } else {
                    numCellsToRemove--;
                }
            }
        }
    }

    private static int countSolutions(String[][] board, Random random) {
        String[][] copy = copyBoard(board);
        int numSolutions = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (copy[row][col].equals(EMPTY_CELL)) {
                    for (int num = 1; num <= SIZE; num++) {
                        String numStr = Integer.toString(num);
                        if (isValid(copy, row, col, numStr)) {
                            copy[row][col] = numStr;
                            numSolutions += countSolutions(copy, random);
                            if (numSolutions > 1) {
                                return numSolutions;
                            }
                            copy[row][col] = EMPTY_CELL;
                        }
                    }
                    return numSolutions;
                }
            }
        }
        return numSolutions + 1;
    }

    private static boolean isValid(String[][] board, int row, int col, String num) {
        // Check if num exists in the same row or column
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i].equals(num) || board[i][col].equals(num)) {
                return false;
            }
        }

        // Check if num exists in the same 3x3 subgrid
        int subgridRow = (row / BLOCK_SIZE) * BLOCK_SIZE;
        int subgridCol = (col / BLOCK_SIZE) * BLOCK_SIZE;
        for (int i = subgridRow; i < subgridRow + BLOCK_SIZE; i++) {
            for (int j = subgridCol; j < subgridCol + BLOCK_SIZE; j++) {
                if (board[i][j].equals(num)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static String[][] copyBoard(String[][] board) {
        String[][] copy = new String[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                copy[row][col] = board[row][col];
            }
        }
        return copy;
    }

    public  String[][] getSolution() {
        return solution;
    }
}
