package com.romanport.rpws.protocol.timeline;

import com.romanport.rpws.entities.TimelineActionType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedFixedList;
import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;
import com.romanport.rpws.protocol.custom_types.FixedList;

import java.util.LinkedList;

public class TimelineAction extends PebbleProtocolSerializedObject {

    @PebbleProtocolSerialized(index = 0)
    public Byte action_id;

    @PebbleProtocolSerialized(index = 1)
    public Byte action_type;

    @PebbleProtocolSerialized(index = 2)
    public Byte attribute_count;

    @PebbleProtocolSerialized(index = 3)
    @PebbleProtocolSerializedFixedList(innerType = TimelineAttribute.class, attribute_count_index = 2, data_length_index = -1)
    public FixedList attributes;

    public TimelineAction() {

    }

    public TimelineAction(int actionId, TimelineActionType type, LinkedList<TimelineAttribute> attributes) {
        action_id = (byte)actionId;
        action_type = (byte)type.GetValue();
        attribute_count = (byte)attributes.size();
        this.attributes = new FixedList(attributes);
    }
}
