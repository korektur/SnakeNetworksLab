package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class MulticastServer implements Runnable {

    private static final Logger LOG = Logger.getLogger(MulticastServer.class.getName());

    private final static String INET_ADDRESS = "FF02::1";
    private final static int PORT = 1234;

    @Override
    public void run() {

        InetAddress address = null; // Get the address that we are going to connect to.
        try {
            address = InetAddress.getByName(INET_ADDRESS);
        } catch (UnknownHostException e) {
            LOG.severe("Error, while getting InetAddress, msg = " + e.getMessage());
        }

        // Open a new DatagramSocket, which will be used to send the data.
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            for (int i = 0; i < 5; i++) {
                String msg = "Sent message no " + i;

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, address, PORT);
                serverSocket.send(msgPacket);

                System.out.println("Server sent packet with msg: " + msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}