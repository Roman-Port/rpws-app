package com.romanport.rpws.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.msgs.PhoneVersionResponse;
import com.romanport.rpws.protocol.msgs.PingReply;
import com.romanport.rpws.protocol.msgs.PingRequest;
import com.romanport.rpws.protocol.msgs.TimeSetUTC;
import com.romanport.rpws.protocol.msgs.WatchVersionRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PebbleDevice implements PebbleTransportInterface {

    public PebbleTransport transport;

    public PebbleDevice() throws Exception {

    }

    public void OnConnectionOpened() {
        try {
            SendMsg(new WatchVersionRequest());
        } catch (Exception ex) {
            RpwsLog.Log("pbl-device-bg", "Error sending watch info request: "+ex.toString());
        }
    }

    public void OnGetMsg(byte[] b) {
        //Decode this as a Pebble Protocol message
        PebblePacket packet = PebblePacket.DecodePacket(b);
        if(packet == null) {
            return;
        }
        RpwsLog.Log("debug", "Decoded packet "+packet.type.toString()+"!");

        //WIP and 100% not final
        try {
            if(packet.type.CompareToName("PHONE_APP_VERSION_REQUEST")) {
                SendMsg(new PhoneVersionResponse());
                //
            } else if(packet.type.CompareToName("PING_REQUEST")){
                SendMsg(new PingReply());//Respond to ping
            }
        } catch (Exception ex) {
            RpwsLog.Log("pbl-device-bg", "Error handling incoming Pebble packet "+packet.type.toString()+": "+ex.toString());
        }
    }

    public void SendMsg(PebblePacket packet) throws Exception {
        byte[] payload;
        try {
            payload = packet.EncodePacket();
            transport.SendMsg(payload);
        } catch (Exception ex) {
            RpwsLog.LogException("pbl-device-bg", "Error encoding Pebble packet: ", ex);
        }
    }

    public void Close() {

    }
}
