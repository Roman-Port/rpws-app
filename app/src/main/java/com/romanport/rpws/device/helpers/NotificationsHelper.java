package com.romanport.rpws.device.helpers;

import android.util.Base64;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.device.PebbleDevice;
import com.romanport.rpws.device.SendMsgGetReplyCallback;
import com.romanport.rpws.entities.NotificationSource;
import com.romanport.rpws.entities.TimelineActionType;
import com.romanport.rpws.entities.TimelineItemType;
import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.blobdb.BlobDatabaseID;
import com.romanport.rpws.protocol.custom_types.FixedList;
import com.romanport.rpws.protocol.timeline.TimelineAction;
import com.romanport.rpws.protocol.timeline.TimelineAttribute;
import com.romanport.rpws.protocol.timeline.TimelineItem;
import com.romanport.rpws.util.ByteEncoder;
import com.romanport.rpws.util.UuidTools;

import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class NotificationsHelper {

    public static void SendModernNotif(PebbleDevice device, String subject, String message, String sender, NotificationSource source, int modernSource, List<TimelineAction> additionalActions) throws Exception {
        //Create attributes
        LinkedList<TimelineAttribute> attributes = new LinkedList<>();
        attributes.add(new TimelineAttribute(0x01, sender.getBytes())); //Sender
        attributes.add(new TimelineAttribute(4, ByteEncoder.FromInt32(modernSource, ByteOrder.LITTLE_ENDIAN))); //Source
        attributes.add(new TimelineAttribute(0x03, message.getBytes())); //Message
        attributes.add(new TimelineAttribute(0x02, subject.getBytes())); //Subject

        //Add actions
        LinkedList<TimelineAction> actions = new LinkedList<>();
        LinkedList<TimelineAttribute> actionAttrib = new LinkedList<>(); //temp
        actionAttrib.add(new TimelineAttribute(0x01, "Dismiss".getBytes())); //temp
        actions.add(new TimelineAction(0, TimelineActionType.Dismiss, actionAttrib));

        //Create timeline item itself
        TimelineItem item = new TimelineItem();
        item.item_id = UUID.randomUUID();//UUID.fromString("183c2d7f-a539-40b6-9ccc-6f6495756954");//UUID.randomUUID();
        item.parent_id = new UUID(0L, 0L);
        item.timestamp = device.GetCurrentUnixTimestamp();
        item.duration = 0;
        item.itemType = (byte)TimelineItemType.NOTIFICATION.GetValue();
        item.flags = 0;
        item.layout = 0x01;
        item.data_length = Short.MAX_VALUE; //Will this work?
        item.attribute_count = (byte)attributes.size();
        item.attributes = new FixedList(attributes);
        item.action_count = (byte)actions.size();
        item.actions = new FixedList(actions);

        //Serialize and send
        byte[] data = item.SerializeObjectToBytes();
        RpwsLog.Log("debug", "Test encoded bytes: "+ Base64.encodeToString(data, Base64.NO_WRAP));

        //Send
        device.dbClient.Insert(BlobDatabaseID.NOTIFICATION, UuidTools.ToBytes(item.item_id), data, new SendMsgGetReplyCallback() {
            @Override
            public void OnReply(PebblePacket p) {
                RpwsLog.Log("debug", "sent!");
            }
        });
    }

}
