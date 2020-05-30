package com.illposed.osc;

import com.illposed.osc.utility.OSCJavaToByteArrayConverter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class OSCMessage extends AbstractOSCPacket {
    private static final Pattern ILLEGAL_ADDRESS_CHAR = Pattern.compile("[ \\#\\*\\,\\?\\[\\]\\{\\}]");
    private String address;
    private List<Object> arguments;

    public /* bridge */ /* synthetic */ byte[] getByteArray() {
        return super.getByteArray();
    }

    public /* bridge */ /* synthetic */ Charset getCharset() {
        return super.getCharset();
    }

    public /* bridge */ /* synthetic */ void setCharset(Charset x0) {
        super.setCharset(x0);
    }

    public OSCMessage() {
        this(null);
    }

    public OSCMessage(String address2) {
        this(address2, null);
    }

    public OSCMessage(String address2, Collection<Object> arguments2) {
        checkAddress(address2);
        this.address = address2;
        if (arguments2 == null) {
            this.arguments = new LinkedList();
        } else {
            this.arguments = new ArrayList(arguments2);
        }
        init();
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        checkAddress(address2);
        this.address = address2;
        contentChanged();
    }

    public void addArgument(Object argument) {
        this.arguments.add(argument);
        contentChanged();
    }

    public List<Object> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    private void computeAddressByteArray(OSCJavaToByteArrayConverter stream) {
        stream.write(this.address);
    }

    private void computeArgumentsByteArray(OSCJavaToByteArrayConverter stream) {
        stream.write(',');
        stream.writeTypes(this.arguments);
        for (Object argument : this.arguments) {
            stream.write(argument);
        }
    }

    /* access modifiers changed from: 0000 */
    public byte[] computeByteArray(OSCJavaToByteArrayConverter stream) {
        computeAddressByteArray(stream);
        computeArgumentsByteArray(stream);
        return stream.toByteArray();
    }

    private static void checkAddress(String address2) {
        if (address2 != null && !isValidAddress(address2)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Not a valid OSC address: ");
            sb.append(address2);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public static boolean isValidAddress(String address2) {
        return address2 != null && address2.startsWith("/") && !address2.contains("//") && !ILLEGAL_ADDRESS_CHAR.matcher(address2).find();
    }
}
