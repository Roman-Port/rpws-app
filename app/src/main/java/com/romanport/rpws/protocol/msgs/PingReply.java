package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class PingReply extends PebblePacket {

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.PING_REPLY;
    }

}
