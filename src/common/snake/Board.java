package common.snake;


import java.io.Serializable;
import java.util.List;

/**
 * @author korektur
 *         26/03/16
 */
public class Board implements Serializable {

    private final Apple apple;
    private final List<Snake> snakes;

    public Board(Apple apple, List<Snake> snakes) {
        this.apple = apple;
        this.snakes = snakes;
    }

    public Apple getApple() {
        return apple;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }
}