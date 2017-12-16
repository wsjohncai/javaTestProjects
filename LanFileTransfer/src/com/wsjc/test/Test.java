package com.wsjc.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Name;

public class Test {

	public static void main(String[] args) {
        try {
            NetworkInterface in = NetworkInterface.getByName("wlan7");
            System.out.println(in.toString());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
