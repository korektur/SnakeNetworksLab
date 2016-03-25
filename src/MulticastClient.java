import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class MulticastClient implements Runnable {

    public static final Logger LOG = Logger.getLogger(MulticastClient.class.getName());

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

        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];

        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);

            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Socket 1 received msg: " + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
