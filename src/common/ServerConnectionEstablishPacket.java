package common;

import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * @author korektur
 *         26/03/16
 */
public class ServerConnectionEstablishPacket {
    InetAddress inetAddress;
    int port;

    ServerConnectionEstablishPacket() {
    }

    private ServerConnectionEstablishPacket(InetAddress address, int port) {
        this.inetAddress = address;
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public static ServerConnectionEstablishPacket extractFromPacket(DatagramPacket packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
        byte[] address = new byte[buffer.limit() - Integer.BYTES];
        buffer.get(address);
        int port = buffer.getInt();
        try {
            return new ServerConnectionEstablishPacket(Inet6Address.getByAddress(address), port);
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Error, while getting InetAddress, msg = " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ServerConnectionEstablishPacket{" +
                "inetAddress=" + inetAddress +
                ", port=" + port +
                '}';
    }
}
