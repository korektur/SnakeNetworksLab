package client;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import common.Constants;
import common.snake.Board;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            serverSocket.setSoTimeout(5000);
            Socket accept = serverSocket.accept();
            OutputStream outputStream = accept.getOutputStream();
            InputStream inputStream = accept.getInputStream();

            objectOutputStream = new ObjectOutputStream(outputStream);
            objectInputStream = new ObjectInputStream(inputStream);

//            EventButtonPressedSender eventButtonPressedSender = new EventButtonPressedSender(objectOutputStream);
//            Thread eventButtonPressedThread = new Thread(eventButtonPressedSender);

            ClientBoardInfoReceiver clientBoardInfoReceiver = new ClientBoardInfoReceiver(objectInputStream);


//            eventButtonPressedThread.start();
            clientBoardInfoReceiver.run();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create event sender for " + port + " beacuse of " + e.getMessage());
        }
    }

    private class ClientBoardInfoReceiver extends JFrame implements Runnable {
        private ObjectInputStream objectInputStream;
        private ClientBoard clientBoard;

        private ClientBoardInfoReceiver(ObjectInputStream objectInputStream) {
            this.objectInputStream = objectInputStream;

            clientBoard = new ClientBoard();
            add(clientBoard);
            setResizable(false);
            pack();

            setTitle("Network snake");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }

        @Override
        public void run() {
            EventQueue.invokeLater(() -> {
                JFrame ex = this;
                ex.setVisible(true);
            });

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Board board = (Board) objectInputStream.readObject();
                    LOG.info("Get board object " + board.toString());
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
                        LOG.info(Thread.currentThread() + "Send button " + ClientBoard.DIRECTION);
                        Thread.sleep(5000);
                        objectOutputStream.flush();
                        ClientBoard.DIRECTION = null;
                    } catch (IOException e) {
                        LOG.severe(e.getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
