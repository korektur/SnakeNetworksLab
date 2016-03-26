package client;

import common.Buttons;
import common.snake.Board;
import common.snake.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static common.Constants.BOARD_HEIGHT;
import static common.Constants.BOARD_WIDTH;

/**
 * Created by Pavel Asadchiy
 * 26.03.16 9:44.
 */
public class ClientBoard extends JPanel implements ActionListener {

    public static Buttons DIRECTION = null;

    private Board boardInfo;

    private final Image ball;
    private final Image apple;
    private final Image head;

    public ClientBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        ImageIcon snakeTail = new ImageIcon("res/dot.png");
        ball = snakeTail.getImage();

        ImageIcon appleImage = new ImageIcon("res/apple.png");
        apple = appleImage.getImage();

        ImageIcon snakeHead = new ImageIcon("res/head.png");
        head = snakeHead.getImage();

        boardInfo = null;
    }

    public ClientBoard updateBoard(Board boardInfo) {
        this.boardInfo = boardInfo;
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawing(g);
    }

    private void drawing(Graphics g) {
        g.drawImage(apple, boardInfo.getApple().getX(), boardInfo.getApple().getY(), this);
        for (Snake snake : boardInfo.getSnakes()) {
            for (int i = 0; i < snake.getX().length; i++) {
                g.drawImage(i == 0 ? head : ball, snake.getX()[i], snake.getY()[i], this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    DIRECTION = Buttons.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    DIRECTION = Buttons.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    DIRECTION = Buttons.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    DIRECTION = Buttons.DOWN;
                    break;
            }
        }
    }
}