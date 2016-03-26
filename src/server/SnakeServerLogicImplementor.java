package server;

import common.Constants;
import common.snake.Apple;
import common.snake.Board;
import common.snake.Snake;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author korektur
 *         26/03/16
 */
class SnakeServerLogicImplementor {

    private ConcurrentMap<Integer, Snake> snakes;
    private int version = 0;
    private Apple apple;

    SnakeServerLogicImplementor() {
        this.snakes = new ConcurrentHashMap<>(Constants.SERVER_MAX_CLIENT_COUNT);
        appleLocationUpdate();
        Thread maintainer = new Thread(new BoardMaintainer());
        maintainer.start();
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

    private void makeStep() {
        checkEatenApple();
        Collection<Snake> values = snakes.values();
        values.forEach(Snake::checkCollision);
        values.forEach(Snake::moveSnake);
        ConcurrentMap<Integer, Snake> snakes = new ConcurrentHashMap<>();
        this.snakes.entrySet().forEach(e -> { if (e.getValue().isInGame()) snakes.put(e.getKey(), e.getValue());});
        this.snakes = snakes;
    }


    ConcurrentMap<Integer, Snake> getSnakes() {
        return snakes;
    }

    public Apple getApple() {
        return apple;
    }

    private class BoardMaintainer implements Runnable{


        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                long startTime = System.currentTimeMillis();
//                System.out.println("SNAKES MOVED" + getBoardSnapshot() );
                SnakeServerLogicImplementor.this.makeStep();
                while(System.currentTimeMillis() - startTime < Constants.SNAKE_DELAY) {
                    try {
                        Thread.sleep(Constants.SNAKE_DELAY - (System.currentTimeMillis() - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    Board getBoardSnapshot() {
        List<Snake> snakeList = new ArrayList<>();
        snakes.values().forEach(snake -> {
            snakeList.add(snake.clone());
        });
        return new Board(apple, snakeList, ++version);
    }
}
