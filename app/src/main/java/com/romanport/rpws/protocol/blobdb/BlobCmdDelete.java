package com.romanport.rpws.protocol.blobdb;

import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedBinaryArray;
import com.romanport.rpws.protocol.custom_types.BinaryArray;

public class BlobCmdDelete extends BlobCmdBase {

    @PebbleProtocolSerialized(index = 2)
    public Byte key_size;

    @PebbleProtocolSerialized(index = 3)
    @PebbleProtocolSerializedBinaryArray(length = -3) //references the above size
    public BinaryArray key;

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.BLOBDB_DELETE;
    }
}
