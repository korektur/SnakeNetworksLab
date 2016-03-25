package server;

import common.Constants;
import common.ServerConnectionEstablishPacket;
import common.snake.Snake;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ConnectionEstablishAnycastServer implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConnectionEstablishAnycastServer.class.getName());

    private final AtomicInteger connectedCnt;
    private final ConcurrentMap<Integer, Snake> snakes;

    public ConnectionEstablishAnycastServer(ConcurrentMap<Integer, Snake> snakes) {
        connectedCnt = new AtomicInteger(0);
        this.snakes = snakes;
    }

    @Override
    public void run() {

        int id_counter = 1;

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

                Thread thread = new Thread(new EventSenderServer(id_counter, extractedPacket.getInetAddress(),
                        extractedPacket.getPort(), snakes));
                thread.start();

                if (connectedCnt.incrementAndGet() >= Constants.SERVER_MAX_CLIENT_COUNT) {
                    LOG.info("Too many clients for this server, leaving group");
                    clientSocket.leaveGroup(address);

                    while (connectedCnt.get() >= Constants.SERVER_MAX_CLIENT_COUNT) {
                        connectedCnt.wait();
                    }

                    LOG.info("Joining group, waiting for clients");
                    clientSocket.joinGroup(address);
                }
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        this.connectedCnt.decrementAndGet();
        connectedCnt.notifyAll();
    }
}