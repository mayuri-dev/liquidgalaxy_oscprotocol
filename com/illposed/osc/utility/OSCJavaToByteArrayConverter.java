package com.illposed.osc.utility;

import com.illposed.osc.OSCImpulse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;

public class OSCJavaToByteArrayConverter {
    protected static final long MSB_0_BASE_TIME = 2085978496000L;
    protected static final long MSB_1_BASE_TIME = -2208988800000L;
    private Charset charset = Charset.defaultCharset();
    private final byte[] intBytes = new byte[4];
    private final byte[] longintBytes = new byte[8];
    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset2) {
        this.charset = charset2;
    }

    public static byte[] alignBigEndToFourByteBoundry(byte[] bytes) {
        int mod = bytes.length % 4;
        if (mod == 0) {
            return bytes;
        }
        int pad = 4 - mod;
        byte[] newBytes = new byte[(bytes.length + pad)];
        System.arraycopy(bytes, 0, newBytes, pad, bytes.length);
        return newBytes;
    }

    public void appendNullCharToAlignStream() {
        int padLen = (4 - (this.stream.size() % 4)) % 4;
        for (int pci = 0; pci < padLen; pci++) {
            this.stream.write(0);
        }
    }

    public byte[] toByteArray() {
        return this.stream.toByteArray();
    }

    public void write(byte[] bytes) {
        writeInteger32ToByteArray(bytes.length);
        writeUnderHandler(bytes);
        appendNullCharToAlignStream();
    }

    public void write(int i) {
        writeInteger32ToByteArray(i);
    }

    public void write(Float f) {
        writeInteger32ToByteArray(Float.floatToIntBits(f.floatValue()));
    }

    public void write(Double d) {
        writeInteger64ToByteArray(Double.doubleToRawLongBits(d.doubleValue()));
    }

    public void write(Integer i) {
        writeInteger32ToByteArray(i.intValue());
    }

    public void write(Long l) {
        writeInteger64ToByteArray(l.longValue());
    }

    public void write(Date timestamp) {
        writeInteger64ToByteArray(javaToNtpTimeStamp(timestamp.getTime()));
    }

    protected static long javaToNtpTimeStamp(long javaTime) {
        long baseTime;
        boolean useBase1 = javaTime < MSB_0_BASE_TIME;
        if (useBase1) {
            baseTime = javaTime - MSB_1_BASE_TIME;
        } else {
            baseTime = javaTime - MSB_0_BASE_TIME;
        }
        long seconds = baseTime / 1000;
        long fraction = ((baseTime % 1000) * 4294967296L) / 1000;
        if (useBase1) {
            seconds |= 2147483648L;
        }
        return (seconds << 32) | fraction;
    }

    public void write(String aString) {
        byte[] stringBytes = aString.getBytes(this.charset);
        byte[] newBytes = new byte[(stringBytes.length + (4 - (aString.length() % 4)))];
        System.arraycopy(stringBytes, 0, newBytes, 0, stringBytes.length);
        try {
            this.stream.write(newBytes);
        } catch (IOException e) {
            throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream", e);
        }
    }

    public void write(Character c) {
        this.stream.write(c.charValue());
        appendNullCharToAlignStream();
    }

    public void write(char c) {
        this.stream.write(c);
    }

    public void write(Object anObject) {
        if (anObject instanceof Collection) {
            for (Object entry : (Collection) anObject) {
                write(entry);
            }
        } else if (anObject instanceof Float) {
            write((Float) anObject);
        } else if (anObject instanceof Double) {
            write((Double) anObject);
        } else if (anObject instanceof String) {
            write((String) anObject);
        } else if (anObject instanceof byte[]) {
            write((byte[]) anObject);
        } else if (anObject instanceof Character) {
            write((Character) anObject);
        } else if (anObject instanceof Integer) {
            write((Integer) anObject);
        } else if (anObject instanceof Long) {
            write((Long) anObject);
        } else if (anObject instanceof Date) {
            write((Date) anObject);
        } else if (!(anObject instanceof OSCImpulse) && !(anObject instanceof Boolean) && anObject != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Do not know how to write an object of class: ");
            sb.append(anObject.getClass());
            throw new RuntimeException(sb.toString());
        }
    }

    public void writeType(Class c) {
        if (Integer.class.equals(c)) {
            this.stream.write(105);
        } else if (Long.class.equals(c)) {
            this.stream.write(104);
        } else if (Date.class.equals(c)) {
            this.stream.write(116);
        } else if (Float.class.equals(c)) {
            this.stream.write(102);
        } else if (Double.class.equals(c)) {
            this.stream.write(100);
        } else if (String.class.equals(c)) {
            this.stream.write(115);
        } else if (byte[].class.equals(c)) {
            this.stream.write(98);
        } else if (Character.class.equals(c)) {
            this.stream.write(99);
        } else if (OSCImpulse.class.equals(c)) {
            this.stream.write(73);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Do not know the OSC type for the java class: ");
            sb.append(c);
            throw new RuntimeException(sb.toString());
        }
    }

    private void writeTypesArray(Collection<Object> array) {
        for (Object element : array) {
            if (Boolean.TRUE.equals(element)) {
                this.stream.write(84);
            } else if (Boolean.FALSE.equals(element)) {
                this.stream.write(70);
            } else {
                writeType(element.getClass());
            }
        }
    }

    public void writeTypes(Collection<Object> types) {
        for (Object type : types) {
            if (type == null) {
                this.stream.write(78);
            } else if (type instanceof Collection) {
                this.stream.write(91);
                writeTypesArray((Collection) type);
                this.stream.write(93);
            } else if (Boolean.TRUE.equals(type)) {
                this.stream.write(84);
            } else if (Boolean.FALSE.equals(type)) {
                this.stream.write(70);
            } else {
                writeType(type.getClass());
            }
        }
        appendNullCharToAlignStream();
    }

    private void writeUnderHandler(byte[] bytes) {
        try {
            this.stream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream");
        }
    }

    private void writeInteger32ToByteArray(int value) {
        byte[] bArr = this.intBytes;
        bArr[3] = (byte) value;
        int value2 = value >>> 8;
        bArr[2] = (byte) value2;
        int value3 = value2 >>> 8;
        bArr[1] = (byte) value3;
        bArr[0] = (byte) (value3 >>> 8);
        try {
            this.stream.write(bArr);
        } catch (IOException ex) {
            throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream", ex);
        }
    }

    private void writeInteger64ToByteArray(long value) {
        byte[] bArr = this.longintBytes;
        bArr[7] = (byte) ((int) value);
        long value2 = value >>> 8;
        bArr[6] = (byte) ((int) value2);
        long value3 = value2 >>> 8;
        bArr[5] = (byte) ((int) value3);
        long value4 = value3 >>> 8;
        bArr[4] = (byte) ((int) value4);
        long value5 = value4 >>> 8;
        bArr[3] = (byte) ((int) value5);
        long value6 = value5 >>> 8;
        bArr[2] = (byte) ((int) value6);
        long value7 = value6 >>> 8;
        bArr[1] = (byte) ((int) value7);
        bArr[0] = (byte) ((int) (value7 >>> 8));
        try {
            this.stream.write(bArr);
        } catch (IOException ex) {
            throw new RuntimeException("You're screwed: IOException writing to a ByteArrayOutputStream", ex);
        }
    }
}
