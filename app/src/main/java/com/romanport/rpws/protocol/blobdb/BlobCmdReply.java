package com.romanport.rpws.protocol.blobdb;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;

public class BlobCmdReply extends PebblePacket {

    @PebbleProtocolSerialized(index = 0)
    public Short token;

    @PebbleProtocolSerialized( index = 1)
    public Byte statusCode;

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.BLOBDB_REPLY;
    }
}
