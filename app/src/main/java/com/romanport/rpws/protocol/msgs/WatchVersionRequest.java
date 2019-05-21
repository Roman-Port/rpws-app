package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.util.DecoderStream;

public class WatchVersionRequest extends PebblePacket {

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.PEBBLE_VERSION_REQUEST;
    }
}
