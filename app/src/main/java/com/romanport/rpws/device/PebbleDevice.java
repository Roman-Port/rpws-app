package com.romanport.rpws.device;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Base64;

import com.google.gson.Gson;
import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketTool;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.blobdb.BlobCmdReply;
import com.romanport.rpws.protocol.blobdb.BlobDBClient;
import com.romanport.rpws.protocol.msgs.PhoneVersionResponse;
import com.romanport.rpws.protocol.msgs.PingReply;
import com.romanport.rpws.protocol.msgs.PingRequest;
import com.romanport.rpws.protocol.msgs.TimeSetUTC;
import com.romanport.rpws.protocol.msgs.WatchVersionReply;
import com.romanport.rpws.protocol.msgs.WatchVersionRequest;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PebbleDevice implements PebbleTransportInterface {

    public PebbleTransport transport;
    public HashMap<Integer, List<SendMsgGetReplyCallback>> waitingGetReplyCallbacks;
    public BlobDBClient dbClient;

    public PebbleDevice() throws Exception {
        waitingGetReplyCallbacks = new HashMap<>();
        dbClient = new BlobDBClient(this);
    }

    public void OnConnectionOpened() throws Exception {

    }

    public void OnGetMsg(byte[] b) {
        //Decode this as a Pebble Protocol message
        PebblePacket packet = PebblePacketTool.DecodePacket(b);
        if(packet == null) {
            return;
        }
        PebblePacketType type = packet.GetPacketType();
        Integer endpoint = type.GetId();
        RpwsLog.Log("debug", "Decoded packet "+type.toString()+"!");

        //If this is currently being waited on, fire there instead of using default methods.
        if(waitingGetReplyCallbacks.containsKey(endpoint)) {
            SendMsgGetReplyCallback cb = waitingGetReplyCallbacks.get(endpoint).get(0); //Get
            waitingGetReplyCallbacks.get(endpoint).remove(0); //Remove

            //Remove the entry entirely if the length is 0
            if(waitingGetReplyCallbacks.get(endpoint).isEmpty())
                waitingGetReplyCallbacks.remove(endpoint);

            //Call callback
            cb.OnReply(packet);
            return;
        }

        //Follow default conditions
        try {
            if(type.CompareToName("PHONE_APP_VERSION_REQUEST")) {
                SendMsg(new PhoneVersionResponse());
            } else if(type.CompareToName("PING_REQUEST")){
                SendMsg(new PingReply());//Respond to ping
            } else if(type.CompareToName("BLOBDB_REPLY")) {
                //Pass this onto the Blob DB client
                dbClient.OnMsgReply((BlobCmdReply)packet);
            }
        } catch (Exception ex) {
            RpwsLog.Log("pbl-device-bg", "Error handling incoming Pebble packet "+type.toString()+": "+ex.toString());
        }
    }

    public void SendMsg(PebblePacket packet) throws Exception {
        byte[] payload;
        try {
            payload = PebblePacketTool.EncodePacket(packet);
            transport.SendMsg(payload);
        } catch (Exception ex) {
            RpwsLog.LogException("pbl-device-bg", "Error encoding Pebble packet: ", ex);
        }
    }

    /**
     * Sends a Pebble Protocol message to the device, then fires a callback on the next message received with the same endpoint
     * @param p Packet to send
     * @param callback Callback to fire
     */
    public void SendMsgGetReply(PebblePacket p, final SendMsgGetReplyCallback callback) throws Exception {
        //Add to waiting queue. Create a new entry if one does not exist
        Integer key = p.GetPacketType().GetId();
        if(waitingGetReplyCallbacks.containsKey(key)) {
            waitingGetReplyCallbacks.get(key).add(callback);
        } else {
            List<SendMsgGetReplyCallback> callbacks = new LinkedList<SendMsgGetReplyCallback>();
            callbacks.add(callback);
            waitingGetReplyCallbacks.put(key, callbacks);
        }

        //Now, actually transmit the message
        SendMsg(p);
    }

    public int GetCurrentUnixTimestamp() {
        return (int)(System.currentTimeMillis() / 1000);
    }

    public void Close() {

    }
}
