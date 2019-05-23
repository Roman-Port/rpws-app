package com.romanport.rpws.entities;

public enum TimelineItemType {

    NOTIFICATION(1),
    PIN(2),
    REMINDER(3);

    int value;
    TimelineItemType(int v) {
        this.value = v;
    }

    public int GetValue() {
        return this.value;
    }

}
