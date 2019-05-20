package com.romanport.rpws.device;

public abstract class PebbleTransport {

    public PebbleTransportInterface outputInterface;

    public PebbleTransport(PebbleTransportInterface i) {
        outputInterface = i;
    }

    public PebbleTransport(){}

    public abstract void OpenConnection() throws Exception;
    public abstract void CloseConnection();
    public abstract void SendMsg(byte[] msg) throws Exception;
}
