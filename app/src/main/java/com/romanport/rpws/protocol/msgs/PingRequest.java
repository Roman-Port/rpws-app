package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class PingRequest extends PebblePacket {

    public PingRequest() {
        this.UpdateInfoFromType(PebblePacketType.PING_REQUEST);
    }

    @Override
    public void DecodePayload(PebblePacketType type, DecoderStream ds) {

    }

    @Override
    public byte[] EncodePayload() {
        EncoderStream es = new EncoderStream(5);
        es.WriteInt32(0);
        es.WriteBool(true); //is idle?
        return es.ToBytes(  );
    }

}
