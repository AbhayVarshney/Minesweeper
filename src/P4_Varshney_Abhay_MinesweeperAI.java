/**
 * Name: Abhay Varshney     Period 4        03/20/16
 *
 * Time Spent: 3 hours
 *
 * Reflection: Overall this lab was a bit difficult because of all the logic that needs
 *             to be sorted out. It requires every sort of move to be carefully thought
 *             about. I was able to use the methods in my other classes to make
 *             the process simpler -- like revealing the board, etc. I wanted to incorporate
 *             patterns in my AI but when I tested out my program, it seems that the
 *             revealObvious and checkGiven methods seeemed to do the trick.
 *
 */

import java.util.ArrayList;
import java.util.Random;

public class P4_Varshney_Abhay_MinesweeperAI {

    int aiCounter;
    P4_Varshney_Abhay_MinesweeperController controller;
    EmptySurroundings check;
    int revealObviousCounter;

    // Constructor
    P4_Varshney_Abhay_MinesweeperAI(P4_Varshney_Abhay_MinesweeperController controller) {
        this.controller = controller;
        check = new EmptySurroundings();
    }

    void run(int aiCounter) {
        this.aiCounter = aiCounter;
        if(aiCounter == 0) {
            revealRandomPosition();
        } else if(aiCounter == 1) {
            checkGiven();
        } else if(aiCounter == 2) {
            revealObvious();
            if(revealObviousCounter == 0) {
                controller.artificialIntelligenceCounter = 1; // 3
                checkGiven();
            }
        }
        controller.callRepaint();
    }

    void revealRandomPosition() { // AI COUNTER 0
        controller.artificialIntelligenceCounter = 0; aiCounter = 0;// assurance...
        Random ran = new Random();
        int myX = ran.nextInt(controller.getBoardSize());
        int myY = ran.nextInt(controller.getBoardSize());
        boolean temp = true;
        while(temp) {
            if (controller.getBoardShow(myX, myY) == controller.view.EMPTY_CELL) {
                controller.userEventUpdate(controller.view.REVEAL, myY, myX);
                temp = false;
            } else {
                myX = ran.nextInt(controller.getBoardSize());
                myY = ran.nextInt(controller.getBoardSize());
            }
        }

        if(check20Percent()) { /** DO THIS IF AND ONLY IF THE BOARD IS AT LEAST 20% filled!!! **/
            controller.artificialIntelligenceCounter++;
        }
    }

    void checkGiven() {
        int counter = 0;
        for (int i = 0; i < controller.getBoardSize(); i++) {
            for (int j = 0; j < controller.getBoardSize(); j++) {
                if(isNumber(i,j)) {
                    // check the surroundings
                    if(check.emptySurroundings(controller.getBoardShow(i, j), i, j)) { // MUST COMPARE PROPERLY!!!
                        // must place a flag here!
                        for (int a = 0; a < check.getSize(); a++) {
                            controller.userEventUpdate(controller.view.FLAG, check.getX(a), check.getY(a));
                            counter++;
                        }
                    }
                }
            }
        }

        if(counter == 0) revealRandomPosition();
        controller.artificialIntelligenceCounter++;
    }

    boolean isNumber(int i, int j) {
        if(controller.getBoardShow(i, j) <= '8' && controller.getBoardShow(i, j) >= '1') { // ensure current position is a #
            return true;
        }
        return false;
    }

    void revealObvious() {        // AI COUNTER 2
        revealObviousCounter = 0;
        for (int i = 0; i < controller.getBoardSize(); i++) {
            for (int j = 0; j < controller.getBoardSize(); j++) {
                if(isNumber(i, j)) {
                    if(check.numOfFlags(i,j) == Character.getNumericValue(controller.getBoardShow(i, j))) {
                        // reveal all the empty cells around it!!!
                        if(check.emptySurroundings('x', i, j)) {}; // just need to store all the empty cell location
                        for (int k = 0; k < check.getSize(); k++) {
                            controller.userEventUpdate(controller.view.REVEAL, check.getX(k), check.getY(k));
                            revealObviousCounter++;
                        }
                    }
                }
            }
        }
    }

    boolean check20Percent() {
        int counter = 0;
        for (int i = 0; i < controller.getBoardSize(); i++) {
            for (int j = 0; j < controller.getBoardSize(); j++) {
                if(controller.getBoardShow(i, j) != controller.view.EMPTY_CELL) counter++;
            }
        }

        if((double)counter / (controller.getBoardSize()*controller.getBoardSize()) < 0.2) return false;
        else return true;
    }

    class EmptySurroundings {
        ArrayList<Integer> emptyCellMyX = new ArrayList<>();
        ArrayList<Integer> emptyCellMyY = new ArrayList<>();

        int[] x = {-1, -1, -1, 1, 1,  1, 0,  0};
        int[] y = { 0, -1,  1, 1, 0, -1, 1, -1};

        boolean emptySurroundings(char element, int myX, int myY) {
            int emptyCellCounter = 0;
            int flagCellCounter = 0;
            this.emptyCellMyX.clear();
            this.emptyCellMyY.clear();

            for (int k = 0; k < x.length; k++) {
                try {
                    if(controller.getBoardShow(myX + x[k], myY + y[k]) == controller.view.EMPTY_CELL) { // '-'
                        emptyCellCounter++;
                        this.emptyCellMyX.add(myX + x[k]);
                        this.emptyCellMyY.add(myY + y[k]);
                    } else if(controller.getBoardShow(myX + x[k], myY + y[k]) == controller.view.FLAG) { // 'f'
                        flagCellCounter++;
                    }
                } catch(IndexOutOfBoundsException e) {}
            }

            if(Character.getNumericValue(element) == (emptyCellCounter + flagCellCounter) &&
               Character.getNumericValue(element) != flagCellCounter) {
                return true;
            }
            else return false;
        }

        int numOfFlags(int myX, int myY) {
            int counter = 0;
            for (int k = 0; k < x.length; k++) {
                try {
                    if(controller.getBoardShow(myX + x[k], myY + y[k]) == controller.view.FLAG) { // 'f'
                        counter++;
                    }
                } catch(IndexOutOfBoundsException e) {}
            }
            return counter;
        }

        int getSize() {
            return emptyCellMyX.size();
        }

        int getX(int x) {
            return this.emptyCellMyX.get(x);
        }

        int getY(int y) {
            return this.emptyCellMyY.get(y);
        }
    }
}
