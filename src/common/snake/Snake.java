package common.snake;

import common.Buttons;
import common.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author korektur
 *         26/03/16
 */
public class Snake implements Serializable {
    private int snakeLength;

    private final List<Integer> x = new ArrayList<>();
    private final List<Integer> y = new ArrayList<>();

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame;

    public Snake() {
        inGame = true;
        snakeLength = 3;
        for (int i = 0; i < snakeLength; i++) {
            x.add(50 - i * 10);
            y.add(50);
        }

    }

    public synchronized boolean eatenApple(Apple apple) {
        if (x.get(0) == apple.x && y.get(0) == apple.y) {
            snakeLength++;
            return true;
        }
        return false;
    }

    public List<Integer> getY() {
        return y;
    }

    public List<Integer> getX() {
        return x;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public synchronized void moveSnake() {

        for (int i = snakeLength - 1; i > 0; i--) {
            x.set(i, x.get(i - 1));
            y.set(i, y.get(i - 1));
        }
        if (leftDirection) {
            x.set(0, x.get(0) - Constants.DOT_SIZE);
        }
        if (rightDirection) {
            x.set(0, x.get(0) + Constants.DOT_SIZE);
        }
        if (upDirection) {
            y.set(0, y.get(0) - Constants.DOT_SIZE);
        }
        if (downDirection) {
            y.set(0, y.get(0) + Constants.DOT_SIZE);
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
            if (i > 4 && Objects.equals(x.get(0), x.get(i)) && Objects.equals(y.get(0), y.get(i))) {
                inGame = false;
            }
        }
        if (y.get(0) >= Constants.BOARD_HEIGHT || y.get(0) < 0 || x.get(0) >= Constants.BOARD_WIDTH || x.get(0) < 0) {
            inGame = false;
        }
    }

    public synchronized boolean isInGame() {
        return inGame;
    }


    @Override
    public String toString() {
        return "Snake{" +
                "snakeLength=" + snakeLength +
                ", x=" + x +
                ", y=" + y +
                ", leftDirection=" + leftDirection +
                ", rightDirection=" + rightDirection +
                ", upDirection=" + upDirection +
                ", downDirection=" + downDirection +
                ", inGame=" + inGame +
                '}';
    }
}
