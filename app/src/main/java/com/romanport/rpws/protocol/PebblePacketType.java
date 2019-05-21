package com.romanport.rpws.protocol;

import android.support.annotation.Nullable;

import com.romanport.rpws.protocol.msgs.DummyPebblePacket;
import com.romanport.rpws.protocol.msgs.PhoneVersionResponse;
import com.romanport.rpws.protocol.msgs.PingReply;
import com.romanport.rpws.protocol.msgs.PingRequest;
import com.romanport.rpws.protocol.msgs.TimeSetUTC;
import com.romanport.rpws.protocol.msgs.WatchVersionRequest;

import java.util.Arrays;
import java.util.Optional;

public enum PebblePacketType {
    TIME_SET_UTC(0x0b, 0x03, TimeSetUTC.class),

    PHONE_APP_VERSION_REQUEST(0x11, 0x00, DummyPebblePacket.class),
    PHONE_APP_VERSION_REPLY(0x11, 0x01, PhoneVersionResponse.class),
    PEBBLE_VERSION_REQUEST(16, 0, WatchVersionRequest.class),
    PEBBLE_VERSION_REPLY(16, 1, DummyPebblePacket.class),

    PING_REQUEST(2001, 0x00, PingRequest.class),
    PING_REPLY(2001, 0x01, PingReply.class);





    private int id;
    private int subId;
    private Class typeClass;
    private PebblePacketType(int id, int subId, Class c) {
        this.id = id;
        this.subId = subId;
        this.typeClass = c;
    }

    @Nullable
    public static PebblePacketType fromId(int value, byte subValue) {
        //Kinda janky
        for(int i = 0; i<values().length; i+=1) {
            if(values()[i].id == value && values()[i].subId == subValue)
                return values()[i];
        }
        return null;
    }

    public int GetId() {
        return this.id;
    }

    public int GetSubId() {
        return this.subId;
    }

    public Class GetTypeClass() {
        return this.typeClass;
    }

    public Boolean CompareToName(String name) {
        return name().equals(name);
    }

    @Override
    public String toString() {
        return name()+"["+GetId()+" ("+GetSubId()+")]";
    }
}
