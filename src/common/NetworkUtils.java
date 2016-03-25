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

    private static final Logger LOG = Logger.getLogger(NetworkUtils .class.getName());

    public static Inet6Address getIPV6() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface networkInterface = en.nextElement();
                if (networkInterface.getDisplayName().equals("wlan") || networkInterface.getDisplayName().equals("wlp3s0")) {
                    for (Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses(); inetAddressEnumeration.hasMoreElements();) {
                        InetAddress inetAddress = inetAddressEnumeration.nextElement();
                        if (inetAddress.getHostAddress().contains(":")) {
                            LOG.info(networkInterface.getDisplayName() + " - " + inetAddress.getHostAddress());
                            return (Inet6Address) inetAddress;
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
}
