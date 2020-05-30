package com.illposed.osc;

import java.nio.charset.Charset;

public interface OSCPacket {
    byte[] getByteArray();

    Charset getCharset();

    void setCharset(Charset charset);
}
