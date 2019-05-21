package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;
import com.romanport.rpws.protocol.PebbleProtocolSerializedString;

public class WatchVersionReply_WatchFirmwareVersion extends PebbleProtocolSerializedObject {

    public WatchVersionReply_WatchFirmwareVersion() {

    }

    @PebbleProtocolSerialized(index = 0)
    public Integer timestamp;

    @PebbleProtocolSerialized(index = 1)
    @PebbleProtocolSerializedString(length = 32)
    public String version_tag;

    @PebbleProtocolSerialized(index = 2)
    @PebbleProtocolSerializedString(length = 8)
    public String git_hash;

    @PebbleProtocolSerialized(index = 3)
    public Boolean is_recovery;

    @PebbleProtocolSerialized(index = 4)
    public Byte hardware_platform;

    @PebbleProtocolSerialized(index = 5)
    public Byte metadata_version;
}