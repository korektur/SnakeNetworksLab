package common.snake;

import common.Buttons;
import common.Constants;

import java.io.Serializable;

/**
 * @author korektur
 *         26/03/16
 */
public class Snake implements Serializable {
    private int snakeLength;

    private final int x[] = new int[Constants.BOARD_WIDTH];
    private final int y[] = new int[Constants.BOARD_HEIGHT];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame;

    public Snake() {
        snakeLength = 3;
        for (int i = 0; i < snakeLength; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

    }

    public synchronized boolean eatenApple(Apple apple) {
        if (x[0] == apple.x && y[0] == apple.y) {
            snakeLength++;
            return true;
        }
        return false;
    }

    public synchronized void moveSnake() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= Constants.DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += Constants.DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= Constants.DOT_SIZE;
        }
        if (downDirection) {
            y[0] += Constants.DOT_SIZE;
        }
    }

    public synchronized void buttonEvent(Buttons button) {
        if (button == Buttons.LEFT && !rightDirection) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if (button == Buttons.RIGHT && !leftDirection) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if (button == Buttons.UP && !downDirection) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
        if (button == Buttons.DOWN && !upDirection) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }

    public synchronized void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (y[0] >= Constants.BOARD_HEIGHT || y[0] < 0 || x[0] >= Constants.BOARD_WIDTH || x[0] < 0) {
            inGame = false;
        }
    }

    public synchronized boolean isInGame() {
        return inGame;
    }
}
