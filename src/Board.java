
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

class Board extends JPanel implements ActionListener {

    private static final int BOARD_WIDTH = 1000;
    private static final int BOARD_HEIGHT = 700;
    private static final int SNAKE_DELAY = 70;
    private static final int DOT_SIZE = 10;

    private static final Random RANDOM = new Random();

    private final int x[] = new int[BOARD_WIDTH];
    private final int y[] = new int[BOARD_HEIGHT];

    private int snakeLength;
    private int appleX;
    private int appleY;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon snakeTail = new ImageIcon("res/dot.png");
        ball = snakeTail.getImage();

        ImageIcon appleImage = new ImageIcon("res/apple.png");
        apple = appleImage.getImage();

        ImageIcon snakeHead = new ImageIcon("res/head.png");
        head = snakeHead.getImage();
    }

    private void initGame() {
        snakeLength = 3;
        for (int i = 0; i < snakeLength; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }
        appleLocationUpdate();
        timer = new Timer(SNAKE_DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawing(g);
    }

    private void drawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < snakeLength; i++) {
                g.drawImage(i == 0 ? head : ball, x[i], y[i], this);
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 24);
        FontMetrics fontMetrics = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - fontMetrics.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
    }

    private void checkEatenApple() {
        if (x[0] == appleX && y[0] == appleY) {
            snakeLength++;
            appleLocationUpdate();
        }
    }

    private void moveSnake() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (y[0] >= BOARD_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= BOARD_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            timer.stop();
        }
    }

    private void appleLocationUpdate() {
        appleX = RANDOM.nextInt(BOARD_WIDTH / DOT_SIZE) * DOT_SIZE;
        appleY = RANDOM.nextInt(BOARD_HEIGHT / DOT_SIZE) * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkEatenApple();
            checkCollision();
            moveSnake();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}