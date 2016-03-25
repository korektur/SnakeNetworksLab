package server;

import common.Constants;
import common.snake.Apple;
import common.snake.Snake;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author korektur
 *         26/03/16
 */
public class SnakeServerLogicImplementor {

    private ConcurrentMap<Integer, Snake> snakes;

    private Apple apple;

    SnakeServerLogicImplementor() {
        this.snakes = new ConcurrentHashMap<>(Constants.SERVER_MAX_CLIENT_COUNT);
        appleLocationUpdate();
    }

    private void checkEatenApple() {
        long count = snakes.values().stream()
                .filter(snake -> snake.eatenApple(apple))
                .count();
        if (count > 0) appleLocationUpdate();
    }

    private void appleLocationUpdate() {
        int x = Constants.RANDOM.nextInt(Constants.BOARD_WIDTH / Constants.DOT_SIZE) * Constants.DOT_SIZE;
        int y = Constants.RANDOM.nextInt(Constants.BOARD_HEIGHT / Constants.DOT_SIZE) * Constants.DOT_SIZE;

        this.apple = new Apple(x, y);
    }

    public void makeStep(ActionEvent e) {
        checkEatenApple();
        Collection<Snake> values = snakes.values();
        values.forEach(Snake::checkCollision);
        values.forEach(Snake::moveSnake);
    }
}
