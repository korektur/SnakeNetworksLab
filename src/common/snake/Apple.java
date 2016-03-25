package common.snake;

/**
 * @author korektur
 *         26/03/16
 */
public class Apple {

    final int x;
    final int y;

    public Apple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
