package common;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * @author korektur
 *         25/03/16
 */
public class Constants {

    private static final Logger LOG = Logger.getLogger(Constants.class.getName());

    public static final int SERVER_IDENTIFICATION_PORT = 1234;
    public static final String ANYCAST_ADDRESS = "FF02::1";
    public static final int SERVER_MAX_CLIENT_COUNT = 4;

    public static Inet6Address getIPV6() throws SocketException {
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
        return null;
    }
}
