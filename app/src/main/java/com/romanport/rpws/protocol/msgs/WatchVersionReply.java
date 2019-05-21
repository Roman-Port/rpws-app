package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.util.DecoderStream;

public class WatchVersionReply extends PebblePacket {

    public WatchVersionReply() {
        this.UpdateInfoFromType(PebblePacketType.PEBBLE_VERSION_REPLY);
    }

    @Override
    public void DecodePayload(PebblePacketType type, DecoderStream ds) {
        //Will never be used. Ignore.
    }

    @Override
    public byte[] EncodePayload() {


    }

}
