package com.romanport.rpws.protocol.blobdb;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.device.PebbleDevice;
import com.romanport.rpws.device.SendMsgGetReplyCallback;
import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.custom_types.BinaryArray;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

public class BlobDBClient {

    public PebbleDevice pebble;
    public HashMap<Short, SendMsgGetReplyCallback> callbacks;
    public Short latestToken;

    public BlobDBClient(PebbleDevice pbl) {
        this.pebble = pbl;
        this.callbacks = new HashMap<>();
        this.latestToken = Short.MIN_VALUE;
    }

    private Short Submit(BlobCmdBase item, SendMsgGetReplyCallback callback) {
        //Generate a token
        Short token = latestToken++;
        item.token = token;

        //Insert callback
        callbacks.put(token, callback);

        try {
            //Encode and send message
            pebble.SendMsg(item);
        } catch (Exception ex) {
            RpwsLog.Log("blobdb-error", "Failed to send Pebble Protocol message.");
        }

        return token;
    }

    public void Insert(BlobDatabaseID database, byte[] key, byte[] value, SendMsgGetReplyCallback callback) {
        BlobCmdInsert cmd = new BlobCmdInsert();
        cmd.databaseId = database.GetIndex();
        cmd.key_size = (byte)key.length;
        cmd.key = new BinaryArray(key);
        cmd.value_size = (short)value.length;
        cmd.value = new BinaryArray(value);

        Submit(cmd, callback);
    }

    public void Delete(BlobDatabaseID database, byte[] key, SendMsgGetReplyCallback callback) {
        BlobCmdDelete cmd = new BlobCmdDelete();
        cmd.databaseId = database.GetIndex();
        cmd.key_size = (byte)key.length;
        cmd.key = new BinaryArray(key);

        Submit(cmd, callback);
    }

    public void Clear(BlobDatabaseID database, SendMsgGetReplyCallback callback) {
        BlobCmdClear cmd = new BlobCmdClear();
        cmd.databaseId = database.GetIndex();

        Submit(cmd, callback);
    }

    public void OnMsgReply(BlobCmdReply p) {
        //Use token to get call callback
        if(callbacks.containsKey(p.token)) {
            SendMsgGetReplyCallback cb = callbacks.get(p.token);
            callbacks.remove(p.token);
            cb.OnReply(p);
        }
    }
}
