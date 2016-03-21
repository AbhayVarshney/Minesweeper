import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class P4_Varshney_Abhay_MinesweeperView extends JFrame {
    /** GUI Purposes**/
    JMenuBar menuBar;
    private JPanel contentPane;
    int[][] panelBoard;
    DrawingPanel board;
    char[][] boardShow;
    boolean isFirst;
    final int WINDOW_WIDTH;
    final int WINDOW_HEIGHT;

    final char BOMB_SYMBOL;
    final char EMPTY_CELL;
    final char FLAG;
    final char QUESTION;
    final char REVEAL;

    /** IMAGE UPLOAD **/
    private BufferedImage smile;
    private BufferedImage mine;

    private BufferedImage mineHit;
    private BufferedImage blank;
    private BufferedImage flag;
    private BufferedImage question;
    private BufferedImage num1;
    private BufferedImage num2;
    private BufferedImage num3;
    private BufferedImage num4;
    private BufferedImage num5;
    private BufferedImage num6;
    private BufferedImage num7;
    private BufferedImage num8;

    /** MENU BAR BUTTONS **/
    JMenuItem mntmHowToPlay;
    JMenuItem mntmAbout;
    JMenuItem mntmNewGame;
    JMenuItem mntmExit;
    JMenu mnOption;
    JMenuItem mntmTotalMines;
    JMenuItem mntmAutoPlay;
    JMenu mnHelp;

    /** STATS **/
    JLabel numOfMines;
    JLabel timeElapsed;

    /** MOUSE **/
    MouseListener mouse;
    int myX;
    int myY;

    /** TIMER **/
    Timer timer;
    long counter;

    private P4_Varshney_Abhay_MinesweeperController controller;
    P4_Varshney_Abhay_MinesweeperView view;
    private int boardSize;
    int cellSize;
    String gameType;
    boolean autoplay;

    // constructor
    P4_Varshney_Abhay_MinesweeperView(P4_Varshney_Abhay_MinesweeperController controller) {
        autoplay = false;
        view = this;
        boardSize = 20; // default...
        cellSize  = 20; // default...
        BOMB_SYMBOL = '*'; // CONSTANTS
        EMPTY_CELL = '-';  // CONSTANTS
        FLAG = 'f';
        QUESTION = '?';
        REVEAL = 'r';
        isFirst = true;
        mouse = new MouseListener();
        counter = 0;

        WINDOW_WIDTH = 450;
        WINDOW_HEIGHT = 600;

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - WINDOW_WIDTH) / 2);
        int y = (int) ((dimension.getHeight() - WINDOW_HEIGHT) / 2);

        this.setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.controller = controller;

        loadImages();
        createUI();
        addActionListeners();
        this.setVisible(true);
    }

    void addActionListeners() {
        mntmAbout.addActionListener(mouse);
        mntmHowToPlay.addActionListener(mouse);
        mntmNewGame.addActionListener(mouse);
        mntmExit.addActionListener(mouse);
        mntmTotalMines.addActionListener(mouse);
        mntmAutoPlay.addActionListener(mouse);
        board.addMouseListener(mouse);
        board.addMouseMotionListener(mouse);
    }

    void loadImages() {
        try {
            smile   = ImageIO.read(new File("images/face_smile.gif"));
            mine    = ImageIO.read(new File("images/bomb_revealed.gif"));
            blank   = ImageIO.read(new File("images/blank.gif"));
            mineHit = ImageIO.read(new File("images/bomb_death.gif"));
            flag    = ImageIO.read(new File("images/bomb_flagged.gif"));
            question= ImageIO.read(new File("images/bomb_question.gif"));
            num1    = ImageIO.read(new File("images/num_1.gif"));
            num2    = ImageIO.read(new File("images/num_2.gif"));
            num3    = ImageIO.read(new File("images/num_3.gif"));
            num4    = ImageIO.read(new File("images/num_4.gif"));
            num5    = ImageIO.read(new File("images/num_5.gif"));
            num6    = ImageIO.read(new File("images/num_6.gif"));
            num7    = ImageIO.read(new File("images/num_7.gif"));
            num8    = ImageIO.read(new File("images/num_8.gif"));
        } catch(IOException e) {
            System.out.println("Couldn't find the file! " + e.getMessage());
            e.getStackTrace();
        }
    }

    void createUI() {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu mnGame = new JMenu("Game");
        menuBar.add(mnGame);

        mntmNewGame = new JMenuItem("New Game");
        mnGame.add(mntmNewGame);

        mntmExit = new JMenuItem("Exit");
        mnGame.add(mntmExit);

        mnOption = new JMenu("Option");
        menuBar.add(mnOption);

        mntmTotalMines = new JMenuItem("Total Mines");
        mnOption.add(mntmTotalMines);

        mntmAutoPlay = new JMenuItem("Auto Play");
        mnOption.add(mntmAutoPlay);

        mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        mntmHowToPlay = new JMenuItem("How to Play");
        mnHelp.add(mntmHowToPlay);

        mntmAbout = new JMenuItem("About");
        mnHelp.add(mntmAbout);

        JMenuItem menuItem_1 = new JMenuItem("");
        menuBar.add(menuItem_1);

        JMenuItem menuItem = new JMenuItem("");
        menuBar.add(menuItem);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setPreferredSize(new Dimension(100,100));

        panelBoard = new int[20][20];
        board = new DrawingPanel();

        JLabel label = new JLabel("Welcome to Minesweeper!");
        contentPane.add(label);

        board.setPreferredSize(new Dimension(20*20+2,20*20+2));
        contentPane.add(board);

        timeElapsed = new JLabel("60");
        timeElapsed.setPreferredSize(new Dimension(120,50));
        TitledBorder title1 = new TitledBorder("Time Elapsed");
        timeElapsed.setBorder(title1);
        contentPane.add(timeElapsed);

        numOfMines = new JLabel("60");
        numOfMines.setPreferredSize(new Dimension(120,50));
        TitledBorder title2 = new TitledBorder("Mines");
        numOfMines.setBorder(title2);
        contentPane.add(numOfMines);

        /** Internal Dialog **/
        Object[] option = {"Hard", "Medium", "Easy"};

        int difficultyLevel = JOptionPane.showOptionDialog(null,
                "Choose the difficulty level!",
                "Minesweeper!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                option,
                option[2]);

        if(difficultyLevel == 0) { // HARD
            gameType = "hard";
            isFirst = false;
        } else if(difficultyLevel == 1) {  // MEDIUM
            gameType = "medium";
            isFirst = false;
        } else { // EASY
            gameType = "easy";
            isFirst = false;
        }
        controller.setGameType(gameType);
        timer = new Timer(1000, new MyTimerActionListener());
        timer.setInitialDelay(1000);
        timer.start();
    }

    void gameOverDialog() {
        // GAME OVER!!!  USER CAN'T CLICK anymore!!!
        timer.stop();
        JOptionPane hitMine = new JOptionPane();
        hitMine.setPreferredSize(new Dimension(100,100));
        int response = hitMine.showInternalConfirmDialog(view.getContentPane(), "Sorry you have hit a mine! \nWould you like to play again?", "Game Over!",
                JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        if(response == 0) hardReset();
        else this.dispose();
    }

    void gameWonDialog() {
        timer.stop();
        board.repaint();
        String wonGameMessage = "Congrats! You beat the game in " + timeElapsed.getText() + " seconds!\n Would you like to play again?";
        String hasCheatedGame = "Boo!!! You cheated! Next time try \n playing without help!!! \nWould you like to try again?";
        JOptionPane wonGame = new JOptionPane();
        int response;
        if(autoplay) {
            response = wonGame.showInternalConfirmDialog(view.getContentPane(), hasCheatedGame, "You cheated :(",
                    JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        } else {

            response = wonGame.showInternalConfirmDialog(view.getContentPane(), wonGameMessage, "You win!",
                    JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        }

        if(response == 0) hardReset(); // okay
        else this.dispose();           // quit
    }

    class DrawingPanel extends JPanel {
        void updateBoardShow() {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    boardShow[i][j] = controller.getBoardShow(i,j); // update new board
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            if(isFirst) { // occurs when the user loads the game first
                for(int i = 0; i < boardSize; i++) {
                    for(int j = 0; j < boardSize; j++) {
                        g.setColor(Color.BLACK);
                        g.drawImage(blank, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        g2.setColor(Color.white);
                        g2.drawRect(cellSize*j, cellSize*i, cellSize, cellSize);
                    }
                }
            } else {
                boardShow = new char[boardSize][boardSize];
                updateBoardShow();

                for(int i = 0; i < boardShow.length; i++) {
                    for(int j = 0; j < boardShow[0].length; j++) {
                        if(boardShow[i][j] == EMPTY_CELL) {
                            g.drawImage(blank, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == FLAG) {
                            g.drawImage(flag, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        }else if(boardShow[i][j] == BOMB_SYMBOL) {
                            g.drawImage(mineHit, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == QUESTION) {
                            g.drawImage(question, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '1') {
                            g.drawImage(num1, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '2') {
                            g.drawImage(num2, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '3') {
                            g.drawImage(num3, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '4') {
                            g.drawImage(num4, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '5') {
                            g.drawImage(num5, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '6') {
                            g.drawImage(num6, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '7') {
                            g.drawImage(num7, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == '8') {
                            g.drawImage(num8, cellSize*j, cellSize*i, cellSize, cellSize, null);
                        } else if(boardShow[i][j] == ' '){
                            g2.setColor(Color.lightGray);
                            g2.fillRect(cellSize*j, cellSize*i, cellSize, cellSize);
                        }
                        g2.setColor(Color.darkGray);
                        g2.drawRect(cellSize*j, cellSize*i, cellSize, cellSize);
                    }
                }
            }
        }
    }

    void setBoardSize(int boardSize, int cellSize) {
        this.boardSize = boardSize;
        this.cellSize = cellSize;
        board.repaint();
    }

    void hardReset() {
        timer.stop();
        Object[] option = {"Hard", "Medium", "Easy"};

        int difficultyLevel = JOptionPane.showOptionDialog(null,
                "Choose the difficulty level!",
                "Minesweeper!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                option,
                option[2]);

        if(difficultyLevel == 0) { // HARD
            gameType = "hard";
        } else if(difficultyLevel == 1) {  // MEDIUM
            gameType = "medium";
        } else { // EASY
            gameType = "easy";
        }

        autoplay = false;
        controller.isNewNum = false;
        controller.setGameType(gameType);
        controller.start();
        controller.resetGame();
        timer.start();
        isFirst = false;
    }

    private class MouseListener extends MouseAdapter implements ActionListener {
        private boolean isRightClick(MouseEvent e) {
            // RIGHT BUTTON --> obtained through stackoverflow
            // source --> http://stackoverflow.com/questions/2972512/how-to-detect-right-click-event-for-mac-os
            return (e.getButton()==MouseEvent.BUTTON3 ||
                    (System.getProperty("os.name").contains("Mac OS X") &&
                            (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 &&
                            (e.getModifiers() & InputEvent.CTRL_MASK) != 0));
        }

        public void mouseReleased(MouseEvent e) {
            int myX = e.getX() / cellSize;
            int myY = e.getY() / cellSize;

            //       MAC                       WINDOWS
            if(isRightClick(e) || SwingUtilities.isRightMouseButton(e)) { // must place a flag here!
                if(controller.getBoardShow(myY, myX) == FLAG) {
                    controller.userEventUpdate(QUESTION, myY, myX);
                } else if(controller.getBoardShow(myY, myX) == QUESTION) {
                    controller.userEventUpdate(EMPTY_CELL, myY, myX);
                } else {
                    controller.userEventUpdate(FLAG, myY, myX);
                    if(controller.getNumOfBombs() < 0) {
                        JOptionPane.showInternalMessageDialog(view.getContentPane(), "You are using too many flags!");
                    }
                }
            } else if(SwingUtilities.isLeftMouseButton(e)) { // must reveal here!
                controller.userEventUpdate(REVEAL, myY, myX);
            }
            board.repaint();
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == mntmHowToPlay) {
                try {
                    JEditorPane helpContent = new JEditorPane(new URL("file:howtoplay.html"));
                    JScrollPane helpPane = new JScrollPane(helpContent);
                    helpPane.setPreferredSize(new Dimension(700,400));
                    JOptionPane.showMessageDialog(null, helpPane, "How To Play", JOptionPane.PLAIN_MESSAGE, null);
                } catch(MalformedURLException a) {
                    System.out.println("file not found! " + a.getMessage());
                    a.getStackTrace();
                } catch(IOException b) {
                    System.out.println("Error: " + b.getMessage());
                    b.getStackTrace();
                }
            } else if(e.getSource() == mntmAbout) {
                try {
                    JEditorPane helpContent = new JEditorPane(new URL("file:about.html"));
                    JScrollPane helpPane = new JScrollPane(helpContent);
                    JOptionPane.showMessageDialog(null, helpPane, "How To Play", JOptionPane.PLAIN_MESSAGE, null);
                } catch(MalformedURLException a) {
                    System.out.println("file not found! " + a.getMessage());
                    a.getStackTrace();
                } catch(IOException b) {
                    System.out.println("Error: " + b.getMessage());
                    b.getStackTrace();
                }
            } else if(e.getSource() == mntmNewGame) { // RESTART THE GAME
                hardReset();
            } else if(e.getSource() == mntmExit) {
                timer.stop();
                view.dispose();
            } else if(e.getSource() == mntmTotalMines) {
                boolean isClear = true;
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        if(controller.getBoardShow(i, j) != EMPTY_CELL) isClear = false;
                    }
                }

                if(!isClear) { // if the user has already made a move, then he/she can't change number of bombs!!!
                    JOptionPane.showInternalMessageDialog(view.getContentPane(), "Sorry you have already started this game! \n Please r" +
                            "eset the game and change the \nnumber of bombs before you make a move!");
                } else {
                    int input = Integer.parseInt(JOptionPane.showInternalInputDialog(view.getContentPane(), "Please input your desired number of bombs!"));
                    controller.changeNumOfBombs(input);
                }
            } else if(e.getSource() == mntmAutoPlay) { // USER WANTS TO AUTO PLAY!!!
                if(!autoplay) {
                    autoplay = true;
                    view.addKeyListener(new MyKeyListener());
                }
//                view.requestFocus();
//                view.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        super.mouseClicked(e);
//                        view.requestFocus();
//                    }
//                });
            }
        }
    }

    private class MyKeyListener extends KeyAdapter{ // AI
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_A && autoplay) { // 'a'
                // CALL AI STEP HERE!!!
                controller.callAI();
            } else {
                removeKeyListener(this);
            }
        }
    }

    class MyTimerActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            timeElapsed.setText(String.valueOf(counter));
            counter++;
        }
    }
}
