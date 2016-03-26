package common;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author korektur
 *         25/03/16
 */
public class Constants {

    public static final int SERVER_IDENTIFICATION_PORT = 1234;
    public static final int CLIENT_LISTENER_PORT = 1235;
    public static final String ANYCAST_ADDRESS = "FF02::1";
    public static final int SERVER_MAX_CLIENT_COUNT = 4;
    public static final int BOARD_WIDTH = 1000;
    public static final int BOARD_HEIGHT = 700;
    public static final int SNAKE_DELAY = 1000;
    public static final int DOT_SIZE = 10;
    public static final Random RANDOM = new Random();

}
