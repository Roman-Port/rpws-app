package com.romanport.rpws.device;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.device.helpers.NotificationsHelper;
import com.romanport.rpws.entities.NotificationSource;
import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.msgs.TimeSetUTC;
import com.romanport.rpws.protocol.msgs.WatchVersionReply;
import com.romanport.rpws.protocol.msgs.WatchVersionRequest;
import com.romanport.rpws.protocol.timeline.TimelineAction;

import java.util.LinkedList;
import java.util.TimeZone;

public class PebbleWatchDevice extends PebbleDevice {

    public WatchVersionReply deviceinfo;
    public String versionTag;
    public Boolean isReady;

    public PebbleWatchDevice() throws Exception {
        super();
        isReady = false;
    }

    @Override
    public void OnConnectionOpened() throws Exception {
        super.OnConnectionOpened();

        //Ask the watch nicely for info
        try {
            RpwsLog.Log("pbl-device-bg", "Connection opened. Requesting watch info...");
            SendMsgGetReply(new WatchVersionRequest(), new SendMsgGetReplyCallback() {
                @Override
                public void OnReply(PebblePacket bp) {
                    //Downloaded version info.
                    WatchVersionReply p = (WatchVersionReply)bp;
                    deviceinfo = p;
                    versionTag = p.running.version_tag.substring(1);
                    isReady = true;

                    //Ready!
                    try {
                        OnDeviceReady();
                    } catch (Exception ex) {
                        RpwsLog.LogException("pbl-device-bg", "Error preparing watch after it was prepared: ",ex);
                    }
                }
            });
        } catch (Exception ex) {
            RpwsLog.Log("pbl-device-bg", "Error sending watch info request: "+ex.toString());
        }
    }

    public void OnDeviceReady() throws Exception {
        //Fetch some less important data in the background. Also update time and do other housekeeping like that.
        UpdateTimeNow();

        //debug
        NotificationsHelper.SendModernNotif(this, "Test", "Test", "Test", NotificationSource.SMS, 45, new LinkedList<TimelineAction>());
    }

    public void UpdateTimeNow() throws Exception {
        //Grab our current TimeZone
        TimeZone tz = TimeZone.getDefault();
        long currentTimeMs = System.currentTimeMillis();
        long utcOffsetMs = tz.getOffset(currentTimeMs);

        //Format the message
        TimeSetUTC setTimeMsg = new TimeSetUTC();
        setTimeMsg.timezoneName = tz.getID();
        setTimeMsg.utcOffset = (short)(utcOffsetMs / 1000 / 60); //utcOffset is in minutes. Convert it.
        setTimeMsg.unixTime = (int) (currentTimeMs / 1000);
        RpwsLog.Log("pbl-device-bg-timeupdate", "Updating Pebble time. UTC offset: "+setTimeMsg.utcOffset+", name: "+setTimeMsg.timezoneName+", time: "+setTimeMsg.unixTime);
        SendMsg(setTimeMsg);
    }
}
