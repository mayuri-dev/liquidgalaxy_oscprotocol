package com.illposed.osc;

import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
import java.nio.charset.Charset;

abstract class AbstractOSCPacket implements OSCPacket {
    private byte[] byteArray = null;
    private Charset charset = Charset.defaultCharset();

    /* access modifiers changed from: 0000 */
    public abstract byte[] computeByteArray(OSCJavaToByteArrayConverter oSCJavaToByteArrayConverter);

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset2) {
        this.charset = charset2;
    }

    private byte[] computeByteArray() {
        OSCJavaToByteArrayConverter stream = new OSCJavaToByteArrayConverter();
        stream.setCharset(this.charset);
        return computeByteArray(stream);
    }

    public byte[] getByteArray() {
        if (this.byteArray == null) {
            this.byteArray = computeByteArray();
        }
        return this.byteArray;
    }

    /* access modifiers changed from: protected */
    public void contentChanged() {
        this.byteArray = null;
    }

    /* access modifiers changed from: protected */
    public void init() {
    }
}
