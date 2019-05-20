package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class PingReply extends PebblePacket {

    public PingReply() {
        this.UpdateInfoFromType(PebblePacketType.PING_REPLY);
    }

    @Override
    public void DecodePayload(PebblePacketType type, DecoderStream ds) {

    }

    @Override
    public byte[] EncodePayload() {
        return new byte[0];
    }

}
