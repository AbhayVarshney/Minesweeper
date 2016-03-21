import java.util.Random;

public class P4_Varshney_Abhay_MinesweeperModel {
    boolean[][] boardView;
    char[][] boardBackground;   /** BEHIND THE SCENE BOARD **/
    char[][] boardShow;         /** WHAT THE USER WILL SEE **/

    boolean gameOver, easy, medium, hard;
    int numOfBombs;
    int bombsLeft;
    int boardSize;
    int posX, posY;

    final char BOMB_SYMBOL;
    final char EMPTY_CELL;
    final char FLAG;


    int[] x = {-1, -1, -1, 1, 1,  1, 0,  0};
    int[] y = { 0, -1,  1, 1, 0, -1, 1, -1};

    // CONSTRUCTOR
    P4_Varshney_Abhay_MinesweeperModel() {
        BOMB_SYMBOL = '*'; // CONSTANTS
        EMPTY_CELL = '-';  // CONSTANTS
        FLAG = 'f';

        gameOver = false;
        easy = false;
        medium = false;
        hard = false;

        boardView = new boolean[100][100];
    }

    boolean hasWonGame() {
        int flagCount = 0;
        for (int i = 0; i < boardShow.length; i++) {
            for (int j = 0; j < boardShow[0].length; j++) {
                if(boardShow[i][j] == FLAG) flagCount++;
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

        if(flagCount != numOfBombs) return false; // user put flags in the wrong position!!!
        return true;
    }

    void assignNumbers() { // works perfectly
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

    void determineNumOfBombs() { // must place random bombs in the board!!!
        if (easy) {
            numOfBombs = 10; // 15
        } else if (medium) {
            numOfBombs = 40;
        } else if (hard) {
            numOfBombs = 80;
        } else { // should come to this situation
            numOfBombs = 100;
        }
        placeRandomBombs(numOfBombs);
    }

    void placeRandomBombs(int bombs) {
        bombsLeft = bombs;
        numOfBombs = bombs;

        for (int i = 0; i < bombs; i++) {
            Random ran = new Random();
            int x = ran.nextInt(boardBackground.length);
            int y = ran.nextInt(boardBackground.length);
            if (boardBackground[x][y] == BOMB_SYMBOL) {
                // star is already there
                while (boardBackground[x][y] == BOMB_SYMBOL) { // continue making random positions!
                    x = ran.nextInt(boardBackground.length);
                    y = ran.nextInt(boardBackground.length);
                }
            }
            // there is a '-' at that position
            boardBackground[x][y] = BOMB_SYMBOL;
        }
    }

    void revealMore(int x, int y) { // RECURSION
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

    void initialize(String gameType, int boardSize) {
        if (gameType.equals("easy")) {
            easy = true;
            medium = false;
            hard = false;
        } else if (gameType.equals("medium")) {
            easy = false;
            medium = true;
            hard = false;
        } else if (gameType.equals("hard")) {
            easy = false;
            medium = false;
            hard = true;
        }

        this.boardSize = boardSize;

        boardBackground = new char[boardSize][boardSize];
        boardShow = new char[boardSize][boardSize];
        for (int i = 0; i < boardBackground.length; i++) {
            for (int j = 0; j < boardBackground[0].length; j++) {
                boardBackground[i][j] = EMPTY_CELL;
                boardShow[i][j] = EMPTY_CELL;
            }
        }
    }

    // GETTER METHOD
    int getBoardSize() { return boardSize; }

    char getBoardShow(int x, int y) {return boardShow[x][y];}

    // SETTER METHOD
    void setPosX(int posX) {
        this.posX = posX;
    }

    void setPosY(int posY) {
        this.posY = posY;
    }

    void setNumBombsLeft(int bombsLeft) { this.bombsLeft = bombsLeft; }
}
