package com.romanport.rpws.util;

import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class DecoderStream {

    private byte[] source;
    public int pos;

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

    public int ReadUShort(ByteOrder order) {
        byte[] buf = ReadBytes(2);
        return ByteDecoder.DecodeUShort(buf, order);
    }

    public short ReadShort(ByteOrder order) {
        byte[] buf = ReadBytes(2);
        return ByteDecoder.DecodeShort(buf, order);
    }

    public int ReadInt(ByteOrder order) {
        byte[] buf = ReadBytes(4);
        return ByteDecoder.DecodeInteger(buf, order);
    }

    public long ReadLong(ByteOrder order) {
        byte[] buf = ReadBytes(8);
        return ByteDecoder.DecodeLong(buf, order);
    }

    public String ReadLengthedString() {
        int length = (int)ReadByte();
        byte[] buf = ReadBytes(length);
        return new String(buf);
    }

    public String ReadConstString(int length) {
        byte[] buf = ReadBytes(length);
        int i = 0;
        for (i = 0; i < buf.length && buf[i] != 0; i++) { } //Find null terminiator
        return new String(buf, 0, i, Charset.defaultCharset());
    }
}
