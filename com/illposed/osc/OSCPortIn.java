package com.illposed.osc;

import com.illposed.osc.utility.OSCByteArrayToJavaConverter;
import com.illposed.osc.utility.OSCPacketDispatcher;
import com.illposed.osc.utility.OSCPatternAddressSelector;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;

public class OSCPortIn extends OSCPort implements Runnable {
    private static final int BUFFER_SIZE = 1536;
    private final OSCByteArrayToJavaConverter converter;
    private final OSCPacketDispatcher dispatcher;
    private boolean listening;

    public OSCPortIn(DatagramSocket socket) {
        super(socket, socket.getLocalPort());
        this.converter = new OSCByteArrayToJavaConverter();
        this.dispatcher = new OSCPacketDispatcher();
    }

    public OSCPortIn(int port) throws SocketException {
        this(new DatagramSocket(port));
    }

    public OSCPortIn(int port, Charset charset) throws SocketException {
        this(port);
        this.converter.setCharset(charset);
    }

    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        DatagramSocket socket = getSocket();
        while (this.listening) {
            try {
                socket.receive(packet);
                this.dispatcher.dispatchPacket(this.converter.convert(buffer, packet.getLength()));
            } catch (SocketException ex) {
                if (this.listening) {
                    throw ex;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startListening() {
        this.listening = true;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void stopListening() {
        this.listening = false;
    }

    public boolean isListening() {
        return this.listening;
    }

    public void addListener(String addressSelector, OSCListener listener) {
        addListener((AddressSelector) new OSCPatternAddressSelector(addressSelector), listener);
    }

    public void addListener(AddressSelector addressSelector, OSCListener listener) {
        this.dispatcher.addListener(addressSelector, listener);
    }
}
