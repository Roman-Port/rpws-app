package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class PhoneVersionResponse extends PebblePacket {

    /*
    protocol_version = Uint32()  # Unused as of v3.0
    session_caps = Uint32()  # Unused as of v3.0
    platform_flags = Uint32()
    response_version = Uint8(default=2)
    major_version = Uint8()
    minor_version = Uint8()
    bugfix_version = Uint8()
    protocol_caps = Uint64()
     */

    @PebbleProtocolSerialized(index = 0)
    public Integer protocol_version;

    @PebbleProtocolSerialized(index = 1)
    public Integer session_caps;

    @PebbleProtocolSerialized(index = 2)
    public Integer platform_flags;

    @PebbleProtocolSerialized(index = 3)
    public Byte response_version;

    @PebbleProtocolSerialized(index = 4)
    public Byte major_version;

    @PebbleProtocolSerialized(index = 5)
    public Byte minor_version;

    @PebbleProtocolSerialized(index = 6)
    public Byte bugfix_version;

    @PebbleProtocolSerialized(index = 7)
    public Long protocol_caps;

    public PhoneVersionResponse() {
        //Write some fake values that are used by libpebble2.
        protocol_version = Integer.MAX_VALUE;
        session_caps = 0;
        platform_flags = 50;
        response_version = 0x02;
        major_version = 3;
        minor_version = 0;
        bugfix_version = 0;
        protocol_caps = Long.MAX_VALUE;
    }

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.PHONE_APP_VERSION_REPLY;
    }
}
