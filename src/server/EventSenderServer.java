package server;

import java.net.InetAddress;

/**
 * @author korektur
 *         26/03/16
 */
public class EventSenderServer implements Runnable {

    private final InetAddress address;
    private final int port;

    public EventSenderServer(InetAddress inetAddress, int port) {
        this.address = inetAddress;
        this.port = port;
    }

    @Override
    public void run() {

    }
}
