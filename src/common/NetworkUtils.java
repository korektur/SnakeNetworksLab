package common;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by Pavel Asadchiy
 * 26.03.16 0:25.
 */
public class NetworkUtils {

    private static final Logger LOG = Logger.getLogger(NetworkUtils.class.getName());

    public static InetAddress getIPV6() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                if (correctNetworkInterface(networkInterface)) {
                    for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress.getHostAddress().contains(":")) {
                            LOG.info(networkInterface.getDisplayName() + " - " + inetAddress.getHostAddress());
                            return inetAddress;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            LOG.severe("Socket exception, msg = " + e.getMessage());
            return null;
        }
        return null;
    }

    private static boolean correctNetworkInterface(NetworkInterface networkInterface) {
        String interfaceName = networkInterface.getDisplayName();
        return interfaceName.equals("wlan0") || interfaceName.equals("eth0")
                || interfaceName.equals("wlp3s0") || interfaceName.equals("enp0s3");
    }
}
