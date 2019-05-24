package com.romanport.rpws.device.services;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.device.PebbleWatchDevice;
import com.romanport.rpws.device.helpers.NotificationsHelper;
import com.romanport.rpws.entities.NotificationSource;
import com.romanport.rpws.protocol.timeline.TimelineAction;

import java.util.LinkedList;

public class BgNotificationService2 extends NotificationListenerService {

    //screw this, I'll come back to it later

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "test 0", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //Toast.makeText(this, "test 1", Toast.LENGTH_LONG).show();
        RpwsLog.Log("notification-service", "Got notification");

        try {
            //Extract string info from the notification
            //Get the app name
            String packageName = sbn.getPackageName();
            ApplicationInfo appInfo = getApplicationContext().getPackageManager().getApplicationInfo(packageName, 0);

            //Get notification info
            Notification not = sbn.getNotification();
            String title = not.extras.getString("android.title");
            String text = not.extras.getString("android.text");

            //Broadcast
            Intent intent = new  Intent("com.romanport.rpws.PEBBLE_NOTIFICATION_ACTION");
            intent.putExtra("action", 1); //1= new notif, 2=remove notif
            intent.putExtra("notif_title", title);
            intent.putExtra("notif_content", text);
            intent.putExtra("notif_sender", appInfo.name);
            sendBroadcast(intent);
            Toast.makeText(this, "test 2", Toast.LENGTH_LONG).show();

            //Transmit
            //NotificationsHelper.SendModernNotif(pebble, title, text, appInfo.name, NotificationSource.SMS, 1, new LinkedList<TimelineAction>());
        } catch (Exception ex) {
            //Toast.makeText(this, "test 3", Toast.LENGTH_LONG).show();
            RpwsLog.LogException("notification-service-err", "Failed to process incoming notification: ", ex);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        RpwsLog.Log("notification-service", "Removed notification");
    }
}
