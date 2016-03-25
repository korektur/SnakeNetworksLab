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
            establishPacketBuilder.setInet6Address(NetworkUtils.getIPV6()).setPort(Constants.SERVER_IDENTIFICATION_PORT);
            DatagramPacket datagramPacket = establishPacketBuilder.build();
            serverSocket.send(datagramPacket);
            LOG.info("Datagram packet " + datagramPacket + " sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
