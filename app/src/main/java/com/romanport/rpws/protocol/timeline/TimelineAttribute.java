package com.romanport.rpws.protocol.timeline;

import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedBinaryArray;
import com.romanport.rpws.protocol.PebbleProtocolSerializedLittleEndian;
import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;
import com.romanport.rpws.protocol.custom_types.BinaryArray;

public class TimelineAttribute extends PebbleProtocolSerializedObject {

    @PebbleProtocolSerialized(index = 0)
    public Byte attribute_id; //this is where the docs in libpebble2 end. I'm very screwed

    @PebbleProtocolSerialized(index = 1)
    @PebbleProtocolSerializedLittleEndian
    public Short length;

    @PebbleProtocolSerialized(index = 2)
    @PebbleProtocolSerializedBinaryArray(length = -1)
    public BinaryArray content;

    public TimelineAttribute() {

    }

    public TimelineAttribute(int id, byte[] data) {
        attribute_id = (byte)id;
        length = (short)data.length;
        content = new BinaryArray(data);
    }
}
