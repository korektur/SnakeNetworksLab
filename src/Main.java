import client.MulticastClient;
import server.MulticastServer;

/**
 * Created by Pavel Asadchiy
 *     25.03.16 13:00.
 */
public class Main {
    public static void main(String[] args) {
        Thread serverThread = new Thread(new MulticastServer());
        Thread clientThread = new Thread(new MulticastClient());

        serverThread.start();
        clientThread.start();

        try {
            serverThread.join();
            clientThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
