import java.util.Random;
import java.util.Scanner;

/**
 * MineSweeper
 * Name: Abhay Varshney   Period 4   3/4/16 - 3/5/16
 * Time Spent: 3 hours
 * Reflection: Overall this lab was kind of hard. The only hard part of this lab
 *             was the recursion. I easily set up the game in about 1 hour but
 *             fixing the recursion issue took me more than 2 hours. I was getting
 *             frustrated with the stack overflow errors. Also I was getting index
 *             out of bounds errors. I tried new code that could possibly work, and
 *             after working on it for 2 hours, I FINALLY got it to work! Whew...
 */

public class P4_Varshney_Abhay_Minesweeper_Console {
    public static void main(String[] args) {
        P4_Varshney_Abhay_Minesweeper_Console.intro();

        Scanner reader = new Scanner(System.in);
        System.out.println("Game Mode: Easy, Medium, or Hard? \t\t");
        String gameType = (reader.next()).toLowerCase();

        MineSweeper game = new MineSweeper(gameType);
        game.play();
    }

    static void intro() {
        System.out.println();
        System.out.println("        _                                                   \n" +
                "  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __ \n" +
                " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n" +
                "/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n" +
                "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n" +
                "                                           |_|              \n");
        System.out.println("Created by Abhay Varshney.");
        System.out.println();
        System.out.println();
    }
}

class MineSweeper {
    boolean gameOver;
    char[][] boardBackground;   /** BEHIND THE SCENE BOARD **/
    char[][] boardShow;         /** WHAT THE USER WILL SEE **/
    boolean[][] boardView;
    boolean easy, medium, hard;
    int boardSize;
    Scanner reader;
    String input;
    int posX, posY;
    int numOfBombs;
    int bombsLeft;

    final char BOMB_SYMBOL;
    final char EMPTY_CELL;
    final char FLAG;

    // constructor
    MineSweeper(String gameType) {
        gameOver = false;
        easy = false;
        medium = false;
        hard = false;

        BOMB_SYMBOL = '*'; // CONSTANTS
        EMPTY_CELL = '-';  // CONSTANTS
        FLAG = 'f';

        reader = new Scanner(System.in);

        boardView = new boolean[10][10];

        // set game mode
        if (gameType.equals("easy")) easy = true;
        else if (gameType.equals("medium")) medium = true;
        else if (gameType.equals("hard")) hard = true;
        else {
            System.out.println("Fine I am going to choose the diffulty!!!");
            boardSize = 30;
        }

        // set board size
        if (easy) boardSize = 8;
        else if (medium) boardSize = 10;
        else if (hard) boardSize = 15;
        boardBackground = new char[boardSize][boardSize];
        boardShow = new char[boardSize][boardSize];
        for (int i = 0; i < boardBackground.length; i++) {
            for (int j = 0; j < boardBackground[0].length; j++) {
                boardBackground[i][j] = EMPTY_CELL;
                boardShow[i][j] = EMPTY_CELL;
            }
        }
    }

    void play() {
        placeRandomBombs();
        assignNumbers(); // board background
        printBoard(boardBackground);
        do {
            printBoard(boardShow);
            promptUser();
            if (input.equals("q")) { // quit the game
                System.out.println("You have quit the game!");
                gameOver = true;
                break;
            } else if (input.equals("r")) { // user opens a tile
                promptPositionPlacement();
                place('r');
            } else if (input.equals("f")) { // user wants to flag a tile
                promptPositionPlacement();
                place(FLAG);
            }
            if(bombsLeft != 0 && !gameOver) {
                printNumOfBombs();
            } else if(hasWonGame()){
                System.out.println("Congrats you have solved MineSweeper!");
                gameOver = true;
            }
        } while (!gameOver);
    }

    boolean hasWonGame() {
        for (int i = 0; i < boardShow.length; i++) {
            for (int j = 0; j < boardShow[0].length; j++) {
                if(boardShow[i][j] == EMPTY_CELL) return false;
            }
        }

        for (int i = 0; i < boardShow.length; i++) {
            for (int j = 0; j < boardShow[0].length; j++) {
                if(boardShow[i][j] == FLAG) {
                    if(boardBackground[i][j] != BOMB_SYMBOL) {
                        return false; // flag not put in the right position :(
                    }
                }
            }
        }
        return true;
    }

    void printNumOfBombs() {
        System.out.println("\n\nThere are a total of " + bombsLeft + " bombs left! \n");
        System.out.println("Good luck!");
    }

    void assignNumbers() { // works perfectly
        int[] x = {-1, -1, -1, 1, 1,  1, 0,  0};
        int[] y = { 0, -1,  1, 1, 0, -1, 1, -1};
        for (int i = 0; i < boardBackground.length; i++) {
            for (int j = 0; j < boardBackground[0].length; j++) {
                if(boardBackground[i][j] == EMPTY_CELL) { // blank spot
                    int counter = 0;
                    for (int k = 0; k < x.length; k++) {
                        try {
                            if(boardBackground[i+x[k]][j+y[k]] == BOMB_SYMBOL) counter++;
                        } catch(IndexOutOfBoundsException e) {}
                    }
                    if(counter == 0) boardBackground[i][j] = ' '; // if there is no bomb next to it!!!
                    else boardBackground[i][j] = Integer.toString(counter).charAt(0);
                }
            }
        }
    }

    void placeRandomBombs() { // must place random bombs in the board!!!
        if (easy) {
            numOfBombs = 7;
        } else if (medium) {
            numOfBombs = 20;
        } else if (hard) {
            numOfBombs = 50;
        }
        bombsLeft = numOfBombs;

        System.out.println();
        printNumOfBombs();
        System.out.println();

        for (int i = 0; i < numOfBombs; i++) {
            Random ran = new Random();
            int x = ran.nextInt(boardSize);
            int y = ran.nextInt(boardSize);
            if (boardBackground[x][y] == BOMB_SYMBOL) {
                // star is already there
                while (boardBackground[x][y] == BOMB_SYMBOL) { // continue making random positions!
                    x = ran.nextInt(boardSize);
                    y = ran.nextInt(boardSize);
                }
            }
            // there is a '-' at that position
            boardBackground[x][y] = BOMB_SYMBOL;
        }
    }

    void promptPositionPlacement() {
        // ask user for what position he/she wants to place the flag
        System.out.println();
        System.out.println("Enter row: (1-" + boardSize + ")?");
        posX = reader.nextInt();
        posX--;
        System.out.println("Enter col: (1-" + boardSize + ")?");
        posY = reader.nextInt();
        posY--;
    }

    void place(char marker) {
        if (boardBackground[posX][posY] == BOMB_SYMBOL && marker == 'r') { // user has clicked on bomb
            System.out.println("\nUh oh! You have hit a bomb at position " + (posX+1) + ", " + (posY+1) + "!");
            boardShow[posX][posY] = BOMB_SYMBOL;
            printBoard(boardShow);
            System.out.println();
            System.out.println("Game over!");
            gameOver = true;
        } else if (boardShow[posX][posY] == FLAG) {
            System.out.println("Sorry you have a flag placed at that position already!");
            System.out.println("Would you like to remove the flag? (y/n)");
            String ans = (reader.next()).toLowerCase();
            if(ans.equals('y')) boardShow[posX][posY] = EMPTY_CELL;
            else System.out.println("\nPlease try again!");
            System.out.println();
        } else {
            // reveal
            if (marker == FLAG) {
                boardShow[posX][posY] = marker;
                bombsLeft--;
            } else {
                // user doesn't think there is a bomb at this position
                // must put in recursive logic
                // marker == 'r'
                if(boardBackground[posX][posY] != ' ') { // if the marker is a #
                    System.out.println(boardBackground[posX][posY]);
                    boardShow[posX][posY] = boardBackground[posX][posY]; // reveal #
                } else {
                    // must use recursion
                    revealMore(posX, posY);
                }
            }
        }
    }

    void revealMore(int x, int y) {
        if (!boardView[x][y]){
            boardShow[x][y] = boardBackground[x][y];
            boardView[x][y]=true;
            if (boardBackground[x][y] == BOMB_SYMBOL)
                gameOver = true;
            else if (boardBackground[x][y] == ' '){
                if (y>0) revealMore(x, y-1);
                if (y<boardSize-1) revealMore(x, y+1);
                if (x>0){
                    revealMore(x-1, y);
                    if (y>0) revealMore(x-1, y-1);
                    if (y<boardSize-1) revealMore(x-1, y+1);
                }
                if (x<boardSize-1){
                    revealMore(x+1, y);
                    if (y>0) revealMore(x+1, y-1);
                    if (y<boardSize-1) revealMore(x+1, y+1);
                }
            }
        }
    }

    void promptUser() {
        boolean correct = false;
        input = ""; // clear input
        do {
            System.out.println("\nWould you like to flag a cell or reveal a cell or quit the game?\n\n" +
                    "Enter 'f' or 'r' or 'q' \t");

            input = reader.next();
            if (input.equals("q") || input.equals("r") || input.equals("f")) {
                correct = true;
            } else {
                System.out.println("Please read the directions properly!");
            }
        } while (!correct);
    }

    void printBoard(char[][] board) { // testing purposes --> print the background board for now
        System.out.print("   ");
        for (int i = 1; i <= boardSize; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.print(i+1 + "  ");
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}