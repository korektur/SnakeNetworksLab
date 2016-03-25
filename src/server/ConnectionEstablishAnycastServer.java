package server;

import common.Constants;
import common.ServerConnectionEstablishPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class ConnectionEstablishAnycastServer implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConnectionEstablishAnycastServer.class.getName());

    @Override
    public void run() {

        InetAddress address; // Get the address that we are going to connect to.
        try {
            address = InetAddress.getByName(Constants.ANYCAST_ADDRESS);
        } catch (UnknownHostException e) {
            LOG.severe("Error, while getting InetAddress, msg = " + e.getMessage());
            throw new IllegalStateException("Error, while getting InetAddress, msg = " + e.getMessage());
        }

        byte[] buf = new byte[256];

        try (MulticastSocket clientSocket = new MulticastSocket(Constants.SERVER_IDENTIFICATION_PORT)) {
            clientSocket.joinGroup(address);

            while (!Thread.currentThread().isInterrupted()) {

                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                ServerConnectionEstablishPacket extractedPacket = ServerConnectionEstablishPacket.extractFromPacket(msgPacket);
                LOG.info("Received packet: " + extractedPacket);
                System.out.println("Socket 1 received msg: " + extractedPacket);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}