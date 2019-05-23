package com.romanport.rpws.protocol.blobdb;

import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedBinaryArray;
import com.romanport.rpws.protocol.custom_types.BinaryArray;

public class BlobCmdClear extends BlobCmdBase {

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.BLOBDB_CLEAR;
    }
}
