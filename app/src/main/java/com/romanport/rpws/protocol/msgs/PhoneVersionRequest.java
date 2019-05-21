package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;

public class PhoneVersionRequest extends PebblePacket {

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.PHONE_APP_VERSION_REQUEST;
    }
}
