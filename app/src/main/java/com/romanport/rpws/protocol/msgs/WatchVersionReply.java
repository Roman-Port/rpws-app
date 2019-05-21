package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedBinaryArray;
import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;
import com.romanport.rpws.protocol.PebbleProtocolSerializedString;
import com.romanport.rpws.protocol.custom_types.BinaryArray;
import com.romanport.rpws.util.DecoderStream;

public class WatchVersionReply extends PebblePacket {

    public WatchVersionReply() {

    }

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.PEBBLE_VERSION_REPLY;
    }

    @PebbleProtocolSerialized(index = 0)
    public WatchVersionReply_WatchFirmwareVersion running;

    @PebbleProtocolSerialized(index = 1)
    public WatchVersionReply_WatchFirmwareVersion recovery;

    @PebbleProtocolSerialized(index = 2)
    public Integer bootloader_timestamp;

    @PebbleProtocolSerialized(index = 3)
    @PebbleProtocolSerializedString(length = 9)
    public String board;

    @PebbleProtocolSerialized(index = 4)
    @PebbleProtocolSerializedString(length = 12)
    public String serial;

    @PebbleProtocolSerialized(index = 5)
    @PebbleProtocolSerializedBinaryArray(length = 6)
    public BinaryArray bt_address;

    @PebbleProtocolSerialized(index = 6)
    public Integer resource_crc;

    @PebbleProtocolSerialized(index = 7)
    public Integer resource_timestamp;

    @PebbleProtocolSerialized(index = 8)
    @PebbleProtocolSerializedString(length = 6)
    public String language;

    @PebbleProtocolSerialized(index = 9)
    public Short language_version;

    @PebbleProtocolSerialized(index = 10)
    public Long capabilities;

    //is_unfaithful listed here as optional? Weird
}
