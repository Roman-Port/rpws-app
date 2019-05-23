package com.romanport.rpws.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteEncoder {

    public static byte[] FromInt32(int v, ByteOrder order) {
        return ByteBuffer.allocate(4).order(order).putInt(v).array();
    }

}
