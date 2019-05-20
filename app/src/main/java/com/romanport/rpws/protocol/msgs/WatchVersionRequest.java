package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.util.DecoderStream;

public class WatchVersionRequest extends PebblePacket {

    public WatchVersionRequest() {
        this.UpdateInfoFromType(PebblePacketType.PEBBLE_VERSION_REQUEST);
    }

    @Override
    public void DecodePayload(PebblePacketType type, DecoderStream ds) {

    }

    @Override
    public byte[] EncodePayload() {
        return new byte[0];
    }

}
