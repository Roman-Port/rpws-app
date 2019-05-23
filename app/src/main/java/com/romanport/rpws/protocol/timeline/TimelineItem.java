package com.romanport.rpws.protocol.timeline;

import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedFixedList;
import com.romanport.rpws.protocol.PebbleProtocolSerializedLittleEndian;
import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;
import com.romanport.rpws.protocol.custom_types.FixedList;

import java.util.UUID;

public class TimelineItem extends PebbleProtocolSerializedObject {

    @PebbleProtocolSerialized(index = 0)
    public UUID item_id;

    @PebbleProtocolSerialized(index = 1)
    public UUID parent_id;

    @PebbleProtocolSerialized(index = 2)
    @PebbleProtocolSerializedLittleEndian
    public Integer timestamp;

    @PebbleProtocolSerialized(index = 3)
    @PebbleProtocolSerializedLittleEndian
    public Short duration;

    @PebbleProtocolSerialized(index = 4)
    public Byte itemType;

    @PebbleProtocolSerialized(index = 5)
    @PebbleProtocolSerializedLittleEndian
    public Short flags;

    @PebbleProtocolSerialized(index = 6)
    public Byte layout;

    @PebbleProtocolSerialized(index = 7)
    @PebbleProtocolSerializedLittleEndian
    public Short data_length;

    @PebbleProtocolSerialized(index = 8)
    public Byte attribute_count;

    @PebbleProtocolSerialized(index = 9)
    public Byte action_count;

    @PebbleProtocolSerialized(index = 10)
    @PebbleProtocolSerializedFixedList(innerType = TimelineAttribute.class, attribute_count_index = 8, data_length_index = 7)
    public FixedList attributes;

    @PebbleProtocolSerialized(index = 11)
    @PebbleProtocolSerializedFixedList(innerType = TimelineAction.class, attribute_count_index = 9, data_length_index = 7)
    public FixedList actions;

}
