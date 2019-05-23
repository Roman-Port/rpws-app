package com.romanport.rpws.protocol.custom_types;

import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class BinaryArray {

    public byte[] data;
    public int length;

    public BinaryArray(int fixedLen) {
        length = fixedLen;
    }
    public BinaryArray(byte[] b) {
        length = b.length;
        data = b;
    }

    public static BinaryArray Read(DecoderStream es, int length) {
        BinaryArray ba = new BinaryArray(length);
        ba.data = es.ReadBytes(length);
        return ba;
    }

    public void Write(EncoderStream es) {
        es.WriteBytes(data);
    }
}
