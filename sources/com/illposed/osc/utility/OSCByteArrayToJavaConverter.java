package com.illposed.osc.utility;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCImpulse;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OSCByteArrayToJavaConverter {
    private static final char BUNDLE_IDENTIFIER = BUNDLE_START.charAt(0);
    private static final String BUNDLE_START = "#bundle";
    private byte[] bytes;
    private int bytesLength;
    private Charset charset = Charset.defaultCharset();
    private int streamPosition;

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset2) {
        this.charset = charset2;
    }

    public OSCPacket convert(byte[] bytes2, int bytesLength2) {
        this.bytes = bytes2;
        this.bytesLength = bytesLength2;
        this.streamPosition = 0;
        if (isBundle()) {
            return convertBundle();
        }
        return convertMessage();
    }

    private boolean isBundle() {
        return this.bytes[0] == BUNDLE_IDENTIFIER;
    }

    private OSCBundle convertBundle() {
        this.streamPosition = BUNDLE_START.length() + 1;
        OSCBundle bundle = new OSCBundle(readTimeTag());
        OSCByteArrayToJavaConverter myConverter = new OSCByteArrayToJavaConverter();
        myConverter.setCharset(this.charset);
        while (this.streamPosition < this.bytesLength) {
            int packetLength = readInteger().intValue();
            if (packetLength == 0) {
                throw new IllegalArgumentException("Packet length may not be 0");
            } else if (packetLength % 4 == 0) {
                byte[] packetBytes = new byte[packetLength];
                System.arraycopy(this.bytes, this.streamPosition, packetBytes, 0, packetLength);
                this.streamPosition += packetLength;
                bundle.addPacket(myConverter.convert(packetBytes, packetLength));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Packet length has to be a multiple of 4, is:");
                sb.append(packetLength);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        return bundle;
    }

    private OSCMessage convertMessage() {
        int i;
        OSCMessage message = new OSCMessage();
        message.setAddress(readString());
        List<Character> types = readTypes();
        if (types == null) {
            return message;
        }
        moveToFourByteBoundry();
        int i2 = 0;
        while (i < types.size()) {
            if ('[' == ((Character) types.get(i)).charValue()) {
                i++;
                message.addArgument(readArray(types, i));
                while (((Character) types.get(i)).charValue() != ']') {
                    i++;
                }
            } else {
                message.addArgument(readArgument(((Character) types.get(i)).charValue()));
            }
            i2 = i + 1;
        }
        return message;
    }

    private String readString() {
        int strLen = lengthOfCurrentString();
        String res = new String(this.bytes, this.streamPosition, strLen, this.charset);
        this.streamPosition += strLen;
        moveToFourByteBoundry();
        return res;
    }

    private byte[] readBlob() {
        int blobLen = readInteger().intValue();
        byte[] res = new byte[blobLen];
        System.arraycopy(this.bytes, this.streamPosition, res, 0, blobLen);
        this.streamPosition += blobLen;
        moveToFourByteBoundry();
        return res;
    }

    private List<Character> readTypes() {
        byte[] bArr = this.bytes;
        int length = bArr.length;
        int i = this.streamPosition;
        if (length <= i || bArr[i] != 44) {
            return null;
        }
        this.streamPosition = i + 1;
        int typesLen = lengthOfCurrentString();
        if (typesLen == 0) {
            return null;
        }
        List<Character> typesChars = new ArrayList<>(typesLen);
        for (int i2 = 0; i2 < typesLen; i2++) {
            byte[] bArr2 = this.bytes;
            int i3 = this.streamPosition;
            this.streamPosition = i3 + 1;
            typesChars.add(Character.valueOf((char) bArr2[i3]));
        }
        return typesChars;
    }

    private Object readArgument(char type) {
        if (type == 'F') {
            return Boolean.FALSE;
        }
        if (type == 'I') {
            return OSCImpulse.INSTANCE;
        }
        if (type == 'N') {
            return null;
        }
        if (type == 'T') {
            return Boolean.TRUE;
        }
        if (type == 'f') {
            return readFloat();
        }
        if (type == 'h') {
            return readLong();
        }
        if (type == 'i') {
            return readInteger();
        }
        switch (type) {
            case 'b':
                return readBlob();
            case 'c':
                return readChar();
            case 'd':
                return readDouble();
            default:
                switch (type) {
                    case 's':
                        return readString();
                    case 't':
                        return readTimeTag();
                    case 'u':
                        return readUnsignedInteger();
                    default:
                        return null;
                }
        }
    }

    private Character readChar() {
        byte[] bArr = this.bytes;
        int i = this.streamPosition;
        this.streamPosition = i + 1;
        return Character.valueOf((char) bArr[i]);
    }

    private BigInteger readBigInteger(int numBytes) {
        byte[] myBytes = new byte[numBytes];
        System.arraycopy(this.bytes, this.streamPosition, myBytes, 0, numBytes);
        this.streamPosition += numBytes;
        return new BigInteger(myBytes);
    }

    private Object readDouble() {
        return Double.valueOf(Double.longBitsToDouble(readBigInteger(8).longValue()));
    }

    private Float readFloat() {
        return Float.valueOf(Float.intBitsToFloat(readBigInteger(4).intValue()));
    }

    private Long readLong() {
        return Long.valueOf(readBigInteger(8).longValue());
    }

    private Integer readInteger() {
        return Integer.valueOf(readBigInteger(4).intValue());
    }

    private Long readUnsignedInteger() {
        byte[] bArr = this.bytes;
        int i = this.streamPosition;
        this.streamPosition = i + 1;
        int firstByte = bArr[i] & 255;
        int i2 = this.streamPosition;
        this.streamPosition = i2 + 1;
        int secondByte = bArr[i2] & 255;
        int i3 = this.streamPosition;
        this.streamPosition = i3 + 1;
        int thirdByte = bArr[i3] & 255;
        int i4 = this.streamPosition;
        this.streamPosition = i4 + 1;
        return Long.valueOf(((long) ((firstByte << 24) | (secondByte << 16) | (thirdByte << 8) | (bArr[i4] & 255))) & 4294967295L);
    }

    private Date readTimeTag() {
        byte[] secondBytes = new byte[8];
        byte[] fractionBytes = new byte[8];
        for (int i = 0; i < 4; i++) {
            secondBytes[i] = 0;
            fractionBytes[i] = 0;
        }
        boolean isImmediate = true;
        for (int i2 = 4; i2 < 8; i2++) {
            byte[] bArr = this.bytes;
            int i3 = this.streamPosition;
            this.streamPosition = i3 + 1;
            secondBytes[i2] = bArr[i3];
            if (secondBytes[i2] > 0) {
                isImmediate = false;
            }
        }
        for (int i4 = 4; i4 < 8; i4++) {
            byte[] bArr2 = this.bytes;
            int i5 = this.streamPosition;
            this.streamPosition = i5 + 1;
            fractionBytes[i4] = bArr2[i5];
            if (i4 < 7) {
                if (fractionBytes[i4] > 0) {
                    isImmediate = false;
                }
            } else if (fractionBytes[i4] > 1) {
                isImmediate = false;
            }
        }
        if (isImmediate) {
            return OSCBundle.TIMESTAMP_IMMEDIATE;
        }
        long secsSince1970 = new BigInteger(secondBytes).longValue() - OSCBundle.SECONDS_FROM_1900_TO_1970;
        long fraction = 0;
        if (secsSince1970 < 0) {
            secsSince1970 = 0;
        }
        long fraction2 = (new BigInteger(fractionBytes).longValue() * 1000) / 4294967296L;
        if (fraction2 > 0) {
            fraction = 1 + fraction2;
        }
        Long.signum(secsSince1970);
        return new Date((1000 * secsSince1970) + fraction);
    }

    private List<Object> readArray(List<Character> types, int pos) {
        int arrayLen = 0;
        while (((Character) types.get(pos + arrayLen)).charValue() != ']') {
            arrayLen++;
        }
        List<Object> array = new ArrayList<>(arrayLen);
        for (int j = 0; j < arrayLen; j++) {
            array.add(readArgument(((Character) types.get(pos + j)).charValue()));
        }
        return array;
    }

    private int lengthOfCurrentString() {
        int len = 0;
        while (this.bytes[this.streamPosition + len] != 0) {
            len++;
        }
        return len;
    }

    private void moveToFourByteBoundry() {
        int i = this.streamPosition;
        this.streamPosition = i + (4 - (i % 4));
    }
}
