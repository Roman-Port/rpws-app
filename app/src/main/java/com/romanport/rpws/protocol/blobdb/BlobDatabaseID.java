package com.romanport.rpws.protocol.blobdb;

public enum BlobDatabaseID {

    TEST(0),
    PIN(1),
    APP(2),
    REMINDER(3),
    NOTIFICATION(4),
    APPGLANCE(11);

    private Byte index;

    BlobDatabaseID(int index) {
        this.index = (byte)index;
    }

    public Byte GetIndex() {
        return index;
    }
}
