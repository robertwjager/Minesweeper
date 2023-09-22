package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Game {
    static int[][] field;
    public static final String START_PROMPT = "How many mines do you want on the field?";
    public static final String FIRST_FIELD_STRING = " |123456789|";
    public static final String SECOND_FIELD_STRING = "-|---------|";
    public static final String USER_INPUT = "Set/unset mines marks or claim a cell as free:";
    public static final String NUMBER_HERE = "There is a number here!";
    public static final String WIN_MESSAGE = "Congratulations! You found all the mines!";

    public static boolean continueOfGame = true;

    public Game(int x, int y) {
        field = new int[x + 2][y + 2];  // Create the game field with a border
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int mineCount = askMineCount(scanner);
        generateField(mineCount);
        printField();
        while (continueOfGame) {
            System.out.println(USER_INPUT);
            userInputAndCheck(scanner);
        }
    }

    private void userInputAndCheck(Scanner scanner) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();

        switch (field[y][x]) {  // Check the cell value at the specified coordinates
            case 1: {  // If the cell has a mine
                field[y][x] = 2;  // Mark the cell as found mine
                if (checkAllMineFound()) {  // Check if all mines are found
                    continueOfGame = false;
                    System.out.println(WIN_MESSAGE);
                } else {
                    printField();
                    return;
                }
            }
            case 0: {  // If the cell is empty
                field[y][x] = -1;  // Mark the cell with a mine mark
                printField();
                return;
            }
            case -1: {  // If the cell has a mine mark
                field[y][x] = 0;  // Remove the mine mark
                printField();
                return;
            }
        }

        if (calculateMineCountAroundCell(x, y) > 0) {  // If there are neighboring mines
            System.out.println(NUMBER_HERE);
        }
    }

    private boolean checkAllMineFound() {
        for (int x = 1; x < field.length - 1; x++) {
            for (int y = 1; y < field[0].length - 1; y++) {
                if (field[x][y] == 1) {
                    return false;  // If there's an unmarked mine left, return false
                }
            }
        }
        return true;  // All mines are found
    }

    private static int askMineCount(Scanner scanner) {
        System.out.println(START_PROMPT);
        return scanner.nextInt();
    }

    private static void printField() {
        System.out.println(FIRST_FIELD_STRING);
        System.out.println(SECOND_FIELD_STRING);
        for (int x = 1; x < field.length - 1; x++) {
            System.out.printf("%d|", x);  // Print row number
            for (int y = 1; y < field[0].length - 1; y++) {
                if (field[x][y] == 0) {
                    int mineCount = calculateMineCountAroundCell(x, y);
                    if (mineCount > 0) {
                        System.out.printf("%d", mineCount);
                    } else {
                        System.out.print(".");
                    }
                } else if (field[x][y] == 1) {
                    System.out.print(".");
                } else if (field[x][y] == 2) {
                    System.out.print("*");
                } else if (field[x][y] < 0) {
                    System.out.print("*");
                }
            }
            System.out.print("|");  // Print end of row
            System.out.println();
        }
        System.out.println(SECOND_FIELD_STRING);
    }

    static int calculateMineCountAroundCell(int x, int y) {
        int mineCount = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (x == i && y == j) {
                    continue;  // Skip the current cell
                }
                if (field[i][j] > 0) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    private static void generateField(int mineCount) {
        Random rnd = new Random();
        while (mineCount > 0) {
            int XcellWithMine = 1 + rnd.nextInt(field.length - 2);  // Random x-coordinate
            int YcellWithMine = 1 + rnd.nextInt(field.length - 2);  // Random y-coordinate
            if (field[XcellWithMine][YcellWithMine] == 0) {
                field[XcellWithMine][YcellWithMine] = 1;
                mineCount--;
            }
        }
    }
}