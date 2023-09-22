package minesweeper;

import java.util.Scanner;

public class MineField {
    private final int SIZE = 9;
    private final int numberOfMines;
    private int numberOfMarkedMines = 0;
    private int minesFound = 0;
    private boolean isLost = false;
    private boolean isStart = true;
    private final char[][] field = new char[SIZE][SIZE];
    private final char[][] playerField = new char[SIZE][SIZE];
    Scanner scanner = new Scanner(System.in);

    public MineField() {
        System.out.print("How many mines do you want on the field? ");
        this.numberOfMines = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = '.';
                playerField[i][j] = field[i][j];
            }
        }
        viewField(playerField);
    }

    private void landMines(int numberOfMines, int[] pos) {
        while (numberOfMines > 0) {
            int i = (int) (Math.random() * SIZE);
            int j = (int) (Math.random() * SIZE);
            if (i == pos[0] && j == pos[1]) {
                continue;
            }
            if (field[i][j] != 'X') {
                field[i][j] = 'X';
                numberOfMines--;
            }
        }
    }

    //Player actions
    public void makeMove() {
        while (!isLost && isInProgress()) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            setOptions();
        }
        if (isLost) {
            System.out.println("You stepped on a mine and failed!");
        } else {
            System.out.println("Congratulations! You found all mines!");
        }
    }

    private boolean isInProgress() {
        return numberOfMines != minesFound &&
                numberOfMines != numberOfMarkedMines &&
                numberOfMines != getFreeCells();
    }

    private void setOptions() {
        String input = scanner.nextLine();
        switch (getMode(input)) {
            case "mine":
                if (markMine(getPosition(input))) {
                    minesFound++;
                }
                break;
            case "free":
                markFree(getPosition(input));
                break;
        }
    }

    private boolean markMine(int[] pos) {
        switch (playerField[pos[0]][pos[1]]) {
            case '*':
                playerField[pos[0]][pos[1]] = '.';
                numberOfMarkedMines--;
                viewField(playerField);
                return false;
            case '.':
                playerField[pos[0]][pos[1]] = '*';
                numberOfMarkedMines++;
                viewField(playerField);
                return field[pos[0]][pos[1]] == 'X';
            default:
                System.out.println("The cell is already occupied!");
                return false;
        }
    }

    private void markFree(int[] pos) {
        if (isStart) {
            landMines(numberOfMines, pos);
            isStart = false;
        }
        switch (playerField[pos[0]][pos[1]]) {
            case '.':
                if (field[pos[0]][pos[1]] == 'X') {
                    viewField(field);
                    isLost = true;
                } else {
                    markMinesNearby(pos);
                    viewField(playerField);
                }
                break;
            default:
                System.out.println("The cell is already occupied");
        }
    }

    private String getMode(String input) {
        return input.split(" ")[2];
    }

    private int[] getPosition(String input) {
        String[] pos = input.trim().split(" ");
        return new int[]{Integer.parseInt(pos[1]) - 1, Integer.parseInt(pos[0]) - 1};
    }

    //View field
    private void viewField(char[][] field) {
        System.out.println("\n |123456789|\n" +
                "-|---------|");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(field[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    //Mark mines
    private void markMinesNearby(int[] pos) {
        char mark = '/';
        int minesAround = getMinesAround(pos);
        if (minesAround > 0) {
            mark = String.valueOf(minesAround).charAt(0);
            fillFields(pos, mark);
        } else {
            fillFields(pos, mark);
            markAsFree(pos);
        }
    }

    private void markAsFree(int[] pos) {
        for (int i = Math.max(0, pos[0] - 1), iMin = Math.min(SIZE - 1, pos[0] + 1); i <= iMin; i++) {
            for (int j = Math.max(0, pos[1] - 1), jMin = Math.min(SIZE - 1, pos[1] + 1); j <= jMin; j++) {
                if (field[i][j] == '/') {
                    continue;
                }
                markMinesNearby(new int[]{i, j});
            }
        }
    }

    private void fillFields(int[] pos, char mark) {
        playerField[pos[0]][pos[1]] = mark;
        field[pos[0]][pos[1]] = mark;
    }

    private int getMinesAround(int[] pos) {
        int n = 0;
        for (int i = Math.max(0, pos[0] - 1), iMin = Math.min(SIZE - 1, pos[0] + 1); i <= iMin; i++) {
            for (int j = Math.max(0, pos[1] - 1), jMin = Math.min(SIZE - 1, pos[1] + 1); j <= jMin; j++) {
                n = field[i][j] == 'X'? n + 1 : n;
            }
        }
        return n;
    }

    //Other
    private int getFreeCells() {
        int freeCells = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                freeCells = playerField[i][j] == '.'? freeCells + 1 : freeCells;
            }
        }
        return freeCells;
    }
}