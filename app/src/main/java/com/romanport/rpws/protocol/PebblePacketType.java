package com.romanport.rpws.protocol;

import android.support.annotation.Nullable;

import com.romanport.rpws.protocol.blobdb.BlobCmdDelete;
import com.romanport.rpws.protocol.blobdb.BlobCmdInsert;
import com.romanport.rpws.protocol.blobdb.BlobCmdReply;
import com.romanport.rpws.protocol.msgs.PhoneVersionRequest;
import com.romanport.rpws.protocol.msgs.PhoneVersionResponse;
import com.romanport.rpws.protocol.msgs.PingReply;
import com.romanport.rpws.protocol.msgs.PingRequest;
import com.romanport.rpws.protocol.msgs.TimeSetUTC;
import com.romanport.rpws.protocol.msgs.WatchVersionReply;
import com.romanport.rpws.protocol.msgs.WatchVersionRequest;

public enum PebblePacketType {

    //IDs are endpoints. Sub IDs are usually one byte that shows it where to go. If a registered command has a sub ID of -1, it means that no sub ID is sent and it should be used instead

    TIME_SET_UTC(0x0b, 0x03, TimeSetUTC.class),

    PHONE_APP_VERSION_REQUEST(0x11, 0x00, PhoneVersionRequest.class),
    PHONE_APP_VERSION_REPLY(0x11, 0x01, PhoneVersionResponse.class),
    PEBBLE_VERSION_REQUEST(16, 0, WatchVersionRequest.class),
    PEBBLE_VERSION_REPLY(16, 1, WatchVersionReply.class),

    BLOBDB_INSERT(0xb1db, 0x01, BlobCmdInsert.class),
    BLOBDB_DELETE(0xb1db, 0x04, BlobCmdDelete.class),
    BLOBDB_CLEAR(0xb1db, 0x05, BlobCmdInsert.class),
    BLOBDB_REPLY(0xb1db, -1, BlobCmdReply.class),

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

    public static Boolean CheckIfEndpointRegistered(int endpoint) {
        for(int i = 0; i<values().length; i+=1) {
            if(values()[i].id == endpoint)
                return true;
        }
        return false;
    }

    @Nullable
    public static PebblePacketType fromId(int value, int subValue) {
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
