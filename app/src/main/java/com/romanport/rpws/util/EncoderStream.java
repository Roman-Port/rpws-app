package com.romanport.rpws.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EncoderStream {

    public byte[] source;
    public int pos;
    public int len;

    public EncoderStream(int length) {
        this.source = new byte[length];
        this.pos = 0;
        this.len = 0;
    }

    public byte[] ToBytes() {
        byte[] b = new byte[len];
        for(int i = 0; i<len; i+=1) {
            b[i] = source[i];
        }
        return b;
    }

    public void UpdateLength() {
        if(pos > len)
            len = pos;
    }

    public void WriteBytes(byte[] b) {
        for(int i = 0; i<b.length; i++) {
            source[pos] = b[i];
            pos++;
        }
        UpdateLength();
    }

    public void WriteByte(byte b) {
        WriteBytes(new byte[]{b});
    }

    public static ByteOrder order = ByteOrder.BIG_ENDIAN; //wtf? why are messages to me little endian

    public void WriteBool(boolean b) {
        if(b)
            WriteByte((byte)1);
        else
            WriteByte((byte)0);
    }

    public void WriteInt32(int v) {
        WriteBytes(ByteBuffer.allocate(4).order(order).putInt(v).array());
    }

    public void WriteInt16(short v) {
        WriteBytes(ByteBuffer.allocate(2).order(order).putShort(v).array());
    }

    public void WriteInt64(long v) {
        WriteBytes(ByteBuffer.allocate(8).order(order).putLong(v).array());
    }

    public void WriteLengthedString(String s) {
        byte[] stringData = s.getBytes();
        WriteByte((byte)stringData.length);
        WriteBytes(stringData);
    }

    public void WriteConstString(String s, int length) throws Exception {
        byte[] stringData = s.getBytes();
        if(stringData.length != length)
            throw new Exception("String sent to WriteConstString did not match the required length!");
        WriteBytes(stringData);
    }
}
