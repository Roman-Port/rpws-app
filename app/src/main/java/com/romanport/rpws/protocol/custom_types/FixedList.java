package com.romanport.rpws.protocol.custom_types;

import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;

import java.util.LinkedList;
import java.util.List;

public class FixedList<T extends PebbleProtocolSerializedObject> {

    public List<T> items;

    public FixedList() {

    }

    public FixedList(List<T> items) {
        this.items = items;
    }

}
