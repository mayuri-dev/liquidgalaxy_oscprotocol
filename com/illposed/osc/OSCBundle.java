package com.illposed.osc;

import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OSCBundle extends AbstractOSCPacket {
    public static final long SECONDS_FROM_1900_TO_1970 = 2208988800L;
    public static final Date TIMESTAMP_IMMEDIATE = new Date(0);
    private List<OSCPacket> packets;
    private Date timestamp;

    public /* bridge */ /* synthetic */ byte[] getByteArray() {
        return super.getByteArray();
    }

    public /* bridge */ /* synthetic */ Charset getCharset() {
        return super.getCharset();
    }

    public /* bridge */ /* synthetic */ void setCharset(Charset x0) {
        super.setCharset(x0);
    }

    public OSCBundle() {
        this(TIMESTAMP_IMMEDIATE);
    }

    public OSCBundle(Date timestamp2) {
        this(null, timestamp2);
    }

    public OSCBundle(Collection<OSCPacket> packets2) {
        this(packets2, TIMESTAMP_IMMEDIATE);
    }

    public OSCBundle(Collection<OSCPacket> packets2, Date timestamp2) {
        if (packets2 == null) {
            this.packets = new LinkedList();
        } else {
            this.packets = new ArrayList(packets2);
        }
        this.timestamp = timestamp2;
        init();
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp2) {
        this.timestamp = timestamp2;
    }

    public void addPacket(OSCPacket packet) {
        this.packets.add(packet);
        contentChanged();
    }

    public List<OSCPacket> getPackets() {
        return Collections.unmodifiableList(this.packets);
    }

    private void computeTimeTagByteArray(OSCJavaToByteArrayConverter stream) {
        Date date = this.timestamp;
        if (date == null || date == TIMESTAMP_IMMEDIATE) {
            stream.write(0);
            stream.write(1);
            return;
        }
        long millisecs = date.getTime();
        long fraction = ((millisecs % 1000) * 4294967296L) / 1000;
        stream.write((int) (SECONDS_FROM_1900_TO_1970 + (millisecs / 1000)));
        stream.write((int) fraction);
    }

    /* access modifiers changed from: 0000 */
    public byte[] computeByteArray(OSCJavaToByteArrayConverter stream) {
        stream.write("#bundle");
        computeTimeTagByteArray(stream);
        for (OSCPacket pkg : this.packets) {
            stream.write(pkg.getByteArray());
        }
        return stream.toByteArray();
    }
}
