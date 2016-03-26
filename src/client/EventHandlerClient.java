package client;

import common.snake.Board;

import java.io.*;
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * @author korektur
 *         26/03/16
 */
public class EventHandlerClient {

    public static final Logger LOG = Logger.getLogger(EventHandlerClient.class.getName());

    private final int port;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    public EventHandlerClient(int port) throws IOException {
        this.port = port;
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(5000);

        OutputStream outputStream = serverSocket.accept().getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);

        InputStream inputStream = serverSocket.accept().getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
    }

    public void start() {
        LOG.info("Client event sender/receiver ready for chatting");
        Thread eventSender = new Thread(new EventSender());
        eventSender.start();

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

    private class EventSender implements Runnable {

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
