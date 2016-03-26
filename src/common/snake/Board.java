package common.snake;


import java.io.Serializable;
import java.util.List;

/**
 * @author korektur
 *         26/03/16
 */
public class Board implements Serializable {

    private final int version;
    private final List<Snake> snakes;
    private final Apple apple;

    public Board(Apple apple, List<Snake> snakes, int version) {
        this.apple = apple;
        this.snakes = snakes;
        this.version = version;
    }

    public Apple getApple() {
        return apple;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    @Override
    public String toString() {
        return "Board{" +
                "version=" + version +
                ", snakes=" + snakes +
                ", apple=" + apple +
                '}';
    }
}
