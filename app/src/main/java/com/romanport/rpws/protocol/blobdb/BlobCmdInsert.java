package com.romanport.rpws.protocol.blobdb;

import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedBinaryArray;
import com.romanport.rpws.protocol.PebbleProtocolSerializedLittleEndian;
import com.romanport.rpws.protocol.custom_types.BinaryArray;

public class BlobCmdInsert extends BlobCmdBase {

    @PebbleProtocolSerialized(index = 2)
    public Byte key_size;

    @PebbleProtocolSerialized(index = 3)
    @PebbleProtocolSerializedBinaryArray(length = -2) //references the above size
    public BinaryArray key;

    @PebbleProtocolSerialized(index = 4)
    @PebbleProtocolSerializedLittleEndian
    public Short value_size;

    @PebbleProtocolSerialized(index = 5)
    @PebbleProtocolSerializedBinaryArray(length = -4) //references the above size
    public BinaryArray value;

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.BLOBDB_INSERT;
    }
}
