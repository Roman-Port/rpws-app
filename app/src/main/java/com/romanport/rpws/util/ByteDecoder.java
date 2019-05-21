package com.romanport.rpws.util;

import java.nio.ByteBuffer;

public class ByteDecoder {

    public static int DecodeUShort(byte[] b) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        int num = wrapped.getShort();
        if(num < 0)
            num += Short.MAX_VALUE;
        return num;
    }

    public static short DecodeShort(byte[] b) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        return wrapped.getShort();
    }

    public static int DecodeInteger(byte[] b) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        return wrapped.getInt();
    }

    public static long DecodeLong(byte[] b) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        return wrapped.getLong();
    }
}
