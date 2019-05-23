package com.romanport.rpws.device;

import com.romanport.rpws.protocol.PebblePacket;

public interface SendMsgGetReplyCallback {

    void OnReply(PebblePacket p);

}
