package com.romanport.rpws.entities;

public enum NotificationSource {

    EMAIL(0),
    SMS(1),
    FACEBOOK(2),
    TWITTER(3);

    int value;
    NotificationSource(int v) {
        this.value = v;
    }

    public int GetValue() {
        return this.value;
    }
}
