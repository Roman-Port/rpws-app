package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
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

    public PhoneVersionResponse() {
        UpdateInfoFromType(PebblePacketType.PHONE_APP_VERSION_REPLY);
    }

    @Override
    public void DecodePayload(PebblePacketType type, DecoderStream ds) {
        //Must be overwritten in a subclass
    }

    @Override
    public byte[] EncodePayload() {
        EncoderStream es = new EncoderStream(24);

        //Write some fake values that are used by libpebble2.
        es.WriteInt32(Integer.MAX_VALUE); //protocol_version
        es.WriteInt32(0); //session_caps
        es.WriteInt32(50); //platform_flags;
        es.WriteByte((byte)0x02); //response_version
        es.WriteByte((byte)3); //response_version
        es.WriteByte((byte)0); //minor_version
        es.WriteByte((byte)0); //bugfix_version
        es.WriteInt64(Long.MAX_VALUE); //protocol_caps

        //Write
        return es.ToBytes();
    }
}
