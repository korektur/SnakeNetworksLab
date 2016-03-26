package client;

import common.Constants;
import common.NetworkUtils;
import common.ServerConnectionEstablishPacketBuilder;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

public class MulticastClient implements Runnable {

    private static final Logger LOG = Logger.getLogger(MulticastClient.class.getName());

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            ServerConnectionEstablishPacketBuilder establishPacketBuilder = new ServerConnectionEstablishPacketBuilder();
            establishPacketBuilder.setInetAddress(NetworkUtils.getIPV6()).setPort(Constants.CLIENT_LISTENER_PORT);
            DatagramPacket datagramPacket = establishPacketBuilder.build();
            serverSocket.send(datagramPacket);
            LOG.info("Datagram packet with IPV6 address and hearing port sent");

            EventHandlerClient eventHandlerClient = new EventHandlerClient(Constants.CLIENT_LISTENER_PORT);
            eventHandlerClient.run();

        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
    }
}
