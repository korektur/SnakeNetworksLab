package server;

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

    EventSenderServer(int id, InetAddress inetAddress, int port, ConcurrentMap<Integer, Snake> snakes) {
        this.id = id;
        this.address = inetAddress;
        this.port = port;
        this.snakes = snakes;
    }

    @Override
    public void run() {
        Snake snake = new Snake();
        snakes.put(id, snake);

        try (Socket sock = new Socket(address, port)) {
            OutputStream ostream = sock.getOutputStream();
            PrintWriter pwrite = new PrintWriter(ostream, true);

            InputStream istream = sock.getInputStream();
            BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

            String receiveMessage, sendMessage;
            while (!Thread.currentThread().isInterrupted()) {
                /*sendMessage = keyRead.readLine();  // keyboard reading
                pwrite.println(sendMessage);       // sending to server
                pwrite.flush();                    // flush the data
                if ((receiveMessage = receiveRead.readLine()) != null) //receive from server
                {
                    System.out.println(receiveMessage); // displaying at DOS prompt
                }*/
            }
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create event sender for " +
                    address + " " + port + " beacuse of " + e.getMessage());
        }
    }

}
