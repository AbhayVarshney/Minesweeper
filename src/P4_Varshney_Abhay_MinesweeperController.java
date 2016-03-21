import java.util.Scanner;

public class P4_Varshney_Abhay_MinesweeperController {

    static P4_Varshney_Abhay_MinesweeperView view;
    static P4_Varshney_Abhay_MinesweeperModel model;
    static P4_Varshney_Abhay_MinesweeperAI artificialInt;

    Scanner reader;
    boolean gameOver;
    String gameType;

    int posX, posY;
    int boardSize, cellSize;
    int artificialIntelligenceCounter;
    boolean isNewNum;

    public P4_Varshney_Abhay_MinesweeperController() {
        gameOver = false;
        isNewNum = false;
        reader = new Scanner(System.in);
        boardSize = 20; // what the user will see at first
        artificialIntelligenceCounter = 0;

        view = new P4_Varshney_Abhay_MinesweeperView(this); // instance create for initial user input
        start();
        model = new P4_Varshney_Abhay_MinesweeperModel();
        play();
        artificialInt = new P4_Varshney_Abhay_MinesweeperAI(this);
    }

    void start() {
        switch (gameType) {
            case "easy"  :  boardSize = 10;
                            cellSize = 40;
                            break;
            case "medium":  boardSize = 15;
                            cellSize = 26;
                            break;
            case "hard"  :  boardSize = 20;
                            cellSize = 20;
                            break;
            default      :  boardSize = 10;
                            cellSize = 40;
                            break;
        }
        view.setBoardSize(boardSize, cellSize);
    }

    void place(char marker) {
        try {
            if (model.boardBackground[posX][posY] == model.BOMB_SYMBOL && marker == 'r') { // user has clicked on bomb
                model.boardShow[posX][posY] = model.BOMB_SYMBOL;
                gameOver = true;
                for (int i = 0; i < model.boardBackground.length; i++) {
                    for (int j = 0; j < model.boardBackground[0].length; j++) {
                        model.boardShow[i][j] = model.boardBackground[i][j];
                    }
                }
                view.board.repaint();
                view.gameOverDialog(); // USER LOST!
            } else if ((model.boardShow[posX][posY] == model.FLAG && marker == view.QUESTION) ||
                    (model.boardShow[posX][posY] == view.QUESTION && marker == view.EMPTY_CELL)){
                model.boardShow[posX][posY] = marker;
            } else { // reveal
                if (marker == model.FLAG) {
                    model.boardShow[posX][posY] = marker;
                    model.setNumBombsLeft(model.bombsLeft-1);
                    view.numOfMines.setText(Integer.toString(model.bombsLeft));
                } else {
                    // user doesn't think there is a bomb at this position
                    // must put in recursive logic
                    // marker == 'r'
                    if(model.boardBackground[posX][posY] != ' ') { // if the marker is a #
                        model.boardShow[posX][posY] = model.boardBackground[posX][posY]; // reveal #
                    } else {
                        // must use recursion
                        model.revealMore(posX, posY);
                    }
                }
            }

            if(model.hasWonGame()) { // Condition --> User has won the game!
                model.boardBackground[posX][posY] = view.FLAG;
                view.gameWonDialog();
            }
        } catch (IndexOutOfBoundsException e) {}
    }

    void play() {
        model.initialize(gameType, boardSize);
        if(!isNewNum) model.determineNumOfBombs();
        model.assignNumbers();
        view.numOfMines.setText(Integer.toString(model.bombsLeft));
    }

    void userEventUpdate(char marker, int myX, int myY) {
        model.setPosX(myX);
        model.setPosY(myY);
        posX = myX;
        posY = myY;

        if(marker == view.REVEAL) place(view.REVEAL);
        else if(marker == view.FLAG) place(view.FLAG);
        else if(marker == view.QUESTION) {
            model.bombsLeft++;
            view.numOfMines.setText(Integer.toString(model.bombsLeft));
            place(view.QUESTION);
        }
        else if(marker == view.EMPTY_CELL) place(view.EMPTY_CELL);
    }


    void resetGame() {
        view.autoplay = false;
        artificialIntelligenceCounter = 0;
        gameOver = false;
        view.counter = 0;
        view.isFirst = true;

        model.gameOver = gameOver;
        model.initialize(gameType, boardSize);
        if(!isNewNum) model.determineNumOfBombs();
        model.assignNumbers();
        view.numOfMines.setText(Integer.toString(model.bombsLeft));

        view.board.repaint(); // update ui

        for (int i = 0; i < model.boardView.length; i++) { // reset board view
            for (int j = 0; j < model.boardView[0].length; j++) {
                model.boardView[i][j] = false;
            }
        }
    }

    void callAI() {
        artificialInt.run(artificialIntelligenceCounter);
    }

    // getter method
    char getBoardShow(int x, int y) {
        return model.getBoardShow(x, y);
    }

    int getNumOfBombs() {
        return model.bombsLeft;
    }

    int getBoardSize() { return model.boardBackground.length; }

    // setter method
    void changeNumOfBombs(int numBombs) {
        isNewNum = true;
        model.initialize(gameType, boardSize);
        model.placeRandomBombs(numBombs);
        model.assignNumbers();
        view.numOfMines.setText(Integer.toString(model.bombsLeft));
        view.board.repaint();
    }

    void setGameType(String gameType) {
        this.gameType = gameType;
    }

    void callRepaint() {
        view.board.repaint();
    }
}

