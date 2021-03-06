package common.snake;

import common.Buttons;
import common.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author korektur
 *         26/03/16
 */
public class Snake implements Serializable {
    private int snakeLength;

    private final List<Integer> x;
    private final List<Integer> y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame;

    public Snake() {
        inGame = true;
        x = new ArrayList<>();
        y = new ArrayList<>();
        snakeLength = 3;
        for (int i = 0; i < snakeLength; i++) {
            x.add(50 - i * 10);
            y.add(50);
        }

    }

    public synchronized boolean eatenApple(Apple apple) {
        if (x.get(0) == apple.x && y.get(0) == apple.y) {
            snakeLength++;
            x.add(x.get(x.size() - 1));
            y.add(y.get(y.size() - 1));
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
        for (int i = x.size() - 1; i > 0; i--) {
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

    public Snake(int snakeLength, List<Integer> x, List<Integer> y, boolean leftDirection, boolean rightDirection, boolean upDirection, boolean downDirection, boolean inGame) {
        this.snakeLength = snakeLength;
        this.x = x;
        this.y = y;
        this.leftDirection = leftDirection;
        this.rightDirection = rightDirection;
        this.upDirection = upDirection;
        this.downDirection = downDirection;
        this.inGame = inGame;
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

    public Snake clone() {
        ArrayList<Integer> x = new ArrayList<>(getX().size());
        ArrayList<Integer> y = new ArrayList<>(getY().size());
        x.addAll(getX());
        y.addAll(getY());
        return new Snake(snakeLength, x, y, leftDirection, rightDirection, upDirection, downDirection, inGame);
    }
}
