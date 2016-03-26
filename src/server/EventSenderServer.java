package server;

import common.Buttons;
import common.Constants;
import common.snake.Board;
import common.snake.Snake;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * @author korektur
 *         26/03/16
 */
class EventSenderServer implements Runnable {

    private static final Logger LOG = Logger.getGlobal();

    private final int id;
    private final InetAddress address;
    private final int port;
    private final ConcurrentMap<Integer, Snake> snakes;
    private final SnakeServerLogicImplementor snakeServerLogicImplementor;
    private final ConnectionEstablishAnycastServer server;
    private Thread receiverThread;

    EventSenderServer(int id, InetAddress inetAddress, int port, ConcurrentMap<Integer, Snake> snakes,
                      SnakeServerLogicImplementor snakeServerLogicImplementor,
                      ConnectionEstablishAnycastServer server) {
        this.id = id;
        this.address = inetAddress;
        this.port = port;
        this.snakes = snakes;
        this.snakeServerLogicImplementor = snakeServerLogicImplementor;
        this.server = server;
    }

    @Override
    public void run() {
        Snake snake = new Snake();
        snakes.put(id, snake);

        try (Socket sock = new Socket(address, port);
             OutputStream ostream = sock.getOutputStream();
             InputStream istream = sock.getInputStream()) {

            LOG.info("Server " + id + " started");
            ClientMessageReceiver receiver = new ClientMessageReceiver(istream);
            receiverThread = new Thread(receiver);
            ClientMessageSender sender = new ClientMessageSender(ostream);
            Thread clientSenderThread = new Thread(sender);
//            receiverThread.start();
            clientSenderThread.run();

        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create event sender for " +
                    address + " " + port + " beacuse of " + e.getMessage());
        }
    }

    private class ClientMessageReceiver implements Runnable {
        private final InputStream is;

        ClientMessageReceiver(InputStream inputStream) {
            this.is = inputStream;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(is)) {

                while (!Thread.currentThread().isInterrupted()) {
                    LOG.info("Waiting for event");
                    Object o = objectInputStream.readObject();
                    if (o instanceof Buttons) {
                        LOG.info("Received button event " + o + " from " + id);
                        snakes.get(id).buttonEvent((Buttons) o);
                    } else {
                        throw new IllegalStateException("Expected Buttons but found another object");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }

    private class ClientMessageSender implements Runnable {

        private final OutputStream os;

        private ClientMessageSender(OutputStream os) {
            this.os = os;
        }

        @Override
        public void run() {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(os)) {
                while (!Thread.currentThread().isInterrupted()) {
                    LOG.info("Sending board");
                    long curTime = System.currentTimeMillis();
                    Board boardSnapshot = snakeServerLogicImplementor.getBoardSnapshot();
                    while (System.currentTimeMillis() - curTime < Constants.SNAKE_DELAY) {
                        try {
                            Thread.sleep(Constants.SNAKE_DELAY - (System.currentTimeMillis() - curTime));
                        } catch (InterruptedException e) {
                            LOG.warning("Cant sleep " + e.getMessage());
                        }
                    }
                    LOG.info("Send snake: " + boardSnapshot);
                    objectOutputStream.writeObject(boardSnapshot);
                    objectOutputStream.flush();

                    if (!snakes.get(id).isInGame()) {
                        snakes.remove(id);
                        LOG.info("Disconnected from server id: " + id);
                        server.disconnect();
                        receiverThread.interrupt();
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }

}
