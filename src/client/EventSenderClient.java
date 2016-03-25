package client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author korektur
 *         26/03/16
 */
public class EventSenderClient implements Runnable {

    private final int port;

    public EventSenderClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket sersock = new ServerSocket(port)) {
            System.out.println("Server  ready for chatting");
            Socket sock = sersock.accept();

            OutputStream ostream = sock.getOutputStream();
            PrintWriter pwrite = new PrintWriter(ostream, true);

            // receiving from server ( receiveRead  object)
            InputStream istream = sock.getInputStream();
            BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

            String receiveMessage, sendMessage;
            while (true) {
                if ((receiveMessage = receiveRead.readLine()) != null) {
                    System.out.println(receiveMessage);
                }

                pwrite.println("");
                pwrite.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create event sender for " +
                    port + " beacuse of " + e.getMessage());
        }
    }
}
