package com.illposed.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class OSCPortOut extends OSCPort {
    private InetAddress address;

    public OSCPortOut(InetAddress address2, int port, DatagramSocket socket) {
        super(socket, port);
        this.address = address2;
    }

    public OSCPortOut(InetAddress address2, int port) throws SocketException {
        this(address2, port, new DatagramSocket());
    }

    public OSCPortOut(InetAddress address2) throws SocketException {
        this(address2, OSCPort.DEFAULT_SC_OSC_PORT);
    }

    public OSCPortOut() throws UnknownHostException, SocketException {
        this(InetAddress.getLocalHost(), OSCPort.DEFAULT_SC_OSC_PORT);
    }

    public void send(OSCPacket aPacket) throws IOException {
        byte[] byteArray = aPacket.getByteArray();
        getSocket().send(new DatagramPacket(byteArray, byteArray.length, this.address, getPort()));
    }
}
