package com.romanport.rpws.entities;

public enum  TimelineActionType {

    AncsDismiss(0x01),
    Generic(0x02),
    Response(0x03),
    Dismiss(0x04),
    HTTP(0x05),
    Snooze(0x06),
    OpenWatchapp(0x07),
    Empty(0x08),
    Remove(0x09),
    OpenPin(0x0a);

    int value;
    TimelineActionType(int v) {
        this.value = v;
    }

    public int GetValue() {
        return this.value;
    }
}
