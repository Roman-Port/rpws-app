package com.romanport.rpws.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteDecoder {

    public static int DecodeUShort(byte[] b, ByteOrder order) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        int num = wrapped.order(order).getShort();
        if(num < 0)
            num += Short.MAX_VALUE;
        return num;
    }

    public static short DecodeShort(byte[] b, ByteOrder order) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        return wrapped.order(order).getShort();
    }

    public static int DecodeInteger(byte[] b, ByteOrder order) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        return wrapped.order(order).getInt();
    }

    public static long DecodeLong(byte[] b, ByteOrder order) {
        ByteBuffer wrapped = ByteBuffer.wrap(b); // big-endian by default
        return wrapped.order(order).getLong();
    }
}
