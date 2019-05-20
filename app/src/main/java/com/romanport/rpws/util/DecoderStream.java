package com.romanport.rpws.util;

import java.lang.reflect.Array;

public class DecoderStream {

    private byte[] source;
    private int pos;

    public DecoderStream(byte[] b) {
        source = b;
        pos = 0;
    }

    public byte[] ReadBytes(int length) {
        //Read these bytes at the current position
        byte[] buf = new byte[length];

        //Copy
        for(int i = 0; i<length; i+=1) {
            buf[i] = source[pos];
            pos++;
        }

        return buf;
    }

    public byte ReadByte() {
        byte b = source[pos];
        pos++;
        return b;
    }

    public int ReadUShort() {
        byte[] buf = ReadBytes(2);
        return ByteDecoder.DecodeUShort(buf);
    }

    public int ReadShort() {
        byte[] buf = ReadBytes(2);
        return ByteDecoder.DecodeShort(buf);
    }
}
