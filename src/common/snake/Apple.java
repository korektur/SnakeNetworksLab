package common.snake;

import java.io.Serializable;

/**
 * @author korektur
 *         26/03/16
 */
public class Apple implements Serializable {

    final int x;
    final int y;

    public Apple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
