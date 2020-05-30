package com.illposed.osc;

import java.net.DatagramSocket;

public abstract class OSCPort {
    public static final int DEFAULT_SC_LANG_OSC_PORT = 57120;
    public static final int DEFAULT_SC_OSC_PORT = 57110;
    private final int port;
    private final DatagramSocket socket;

    protected OSCPort(DatagramSocket socket2, int port2) {
        this.socket = socket2;
        this.port = port2;
    }

    public static int defaultSCOSCPort() {
        return DEFAULT_SC_OSC_PORT;
    }

    public static int defaultSCLangOSCPort() {
        return DEFAULT_SC_LANG_OSC_PORT;
    }

    /* access modifiers changed from: protected */
    public DatagramSocket getSocket() {
        return this.socket;
    }

    /* access modifiers changed from: protected */
    public int getPort() {
        return this.port;
    }

    public void close() {
        this.socket.close();
    }
}
