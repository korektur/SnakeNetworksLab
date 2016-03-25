package common;

import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * @author korektur
 *         25/03/16
 */
public class ServerConnectionEstablishPacketBuilder {

    private Inet6Address inet6Address;
    private int port;

    public ServerConnectionEstablishPacketBuilder() {
    }

    public ServerConnectionEstablishPacketBuilder setInet6Address(Inet6Address inet6Address) {
        this.inet6Address = inet6Address;
        return this;
    }

    public ServerConnectionEstablishPacketBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public DatagramPacket build() {
        ByteBuffer buffer = ByteBuffer.allocate(inet6Address.getAddress().length + Integer.BYTES);
        buffer.put(inet6Address.getAddress());
        buffer.putInt(port);
        try {
            InetAddress inetAddress = InetAddress.getByName(Constants.ANYCAST_ADDRESS);
            return new DatagramPacket(buffer.array(), buffer.limit(), inetAddress, Constants.SERVER_IDENTIFICATION_PORT);
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Error, while getting InetAddress, msg = " + e.getMessage());
        }


    }
}
