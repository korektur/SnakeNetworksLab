package client;

import common.snake.Board;

import java.io.*;
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * @author korektur
 *         26/03/16
 */
public class EventHandlerClient implements Runnable {

    public static final Logger LOG = Logger.getLogger(EventHandlerClient.class.getName());

    private final int port;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public EventHandlerClient(int port) throws IOException {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             OutputStream outputStream = serverSocket.accept().getOutputStream();
             InputStream inputStream = serverSocket.accept().getInputStream()) {

            objectOutputStream = new ObjectOutputStream(outputStream);
            objectInputStream = new ObjectInputStream(inputStream);

            EventButtonPressedSender eventButtonPressedSender = new EventButtonPressedSender(objectOutputStream);
            Thread eventButtonPressedThread = new Thread(eventButtonPressedSender);

            ClientBoardInfoReceiver clientBoardInfoReceiver = new ClientBoardInfoReceiver(objectInputStream);
            Thread clientBoardInfoThread = new Thread(clientBoardInfoReceiver);

            eventButtonPressedThread.start();
            clientBoardInfoThread.run();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create event sender for " + port + " beacuse of " + e.getMessage());
        }
    }

    private class ClientBoardInfoReceiver implements Runnable {
        private ObjectInputStream objectInputStream;

        private ClientBoardInfoReceiver(ObjectInputStream objectInputStream) {
            this.objectInputStream = objectInputStream;
        }

        @Override
        public void run() {
            ClientBoard clientBoard = new ClientBoard();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Board board = (Board) objectInputStream.readObject();
                    clientBoard.updateBoard(board);
                    clientBoard.repaint();
                } catch (ClassNotFoundException | IOException e) {
                    LOG.severe("Couldn't create event sender for " + port + " because of " + e.getMessage());
                }
            }
        }
    }

    private class EventButtonPressedSender implements Runnable {
        private ObjectOutputStream objectOutputStream;

        private EventButtonPressedSender(ObjectOutputStream objectOutputStream) {
            this.objectOutputStream = objectOutputStream;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (ClientBoard.DIRECTION != null) {
                    try {
                        objectOutputStream.writeObject(ClientBoard.DIRECTION);
                        objectOutputStream.flush();
                        ClientBoard.DIRECTION = null;
                    } catch (IOException e) {
                        LOG.severe(e.getMessage());
                    }
                }
            }
        }
    }
}
