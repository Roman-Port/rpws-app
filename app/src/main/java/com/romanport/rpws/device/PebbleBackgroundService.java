package com.romanport.rpws.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.romanport.rpws.R;
import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.bluetooth.BluetoothTransport;
import com.romanport.rpws.device.helpers.NotificationsHelper;
import com.romanport.rpws.device.services.BgNotificationService2;
import com.romanport.rpws.entities.NotificationSource;
import com.romanport.rpws.protocol.timeline.TimelineAction;

import java.util.LinkedList;

public class PebbleBackgroundService extends Service {

    public PebbleDevice pblDevice;
    public Notification bgNotif;
    public PebbleNotificationChange serviceNotifications;

    public PebbleBackgroundService() {

    }

    public static void StartBgService(Context c) {
        //Trigger
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            c.startForegroundService(new Intent(c, PebbleBackgroundService.class));
        } else {
            c.startService(new Intent(c, PebbleBackgroundService.class));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        FinishInit();
        InitConnection();
        return Service.START_STICKY;
    }

    private NotificationCompat.Builder GetDefaultNotif() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "rpws-persist")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.persist_notification_title_working))
                .setContentText(getString(R.string.persist_notification_body))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true);
        return builder;
    }

    private void FinishInit() {
        //Construct the notification

        //Create the NotificationChannel, but only on API 26+ because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Persistent Notification";
            String description = "Notification required to keep this app running in the background.";
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel("rpws-persist", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //Build notification
        NotificationCompat.Builder builder = GetDefaultNotif();

        //Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification n = builder.build();
        bgNotif = n;
        notificationManager.notify(1, n);
        startForeground(1, n);

        //Start some other services, such as the notification service
        serviceNotifications = new PebbleNotificationChange();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.romanport.rpws.PEBBLE_NOTIFICATION_ACTION");
        registerReceiver(serviceNotifications,intentFilter);
        startService(new Intent(this, BgNotificationService2.class));
    }

    public class PebbleNotificationChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Get the type
            int actionType = intent.getIntExtra("type", -1);
            try {
                if(actionType == 1) {
                    //Notification was sent.
                    String title = intent.getStringExtra("notif_title");
                    String content = intent.getStringExtra("notif_content");
                    String sender = intent.getStringExtra("notif_sender");
                    NotificationsHelper.SendModernNotif(pblDevice, title, content, sender, NotificationSource.SMS, 1, new LinkedList<TimelineAction>());
                }
            } catch (Exception ex) {
                RpwsLog.LogException("bg-notification-rx-err", "Got error: ", ex);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void InitConnection() {
        RpwsLog.Log("background-bt", "Starting the background service...");

        //Open Bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //TODO: Handle Bluetooth not existing
            SetBtStatus(false, false, false);
            return;
        }

        //Start up Bluetooth if needed
        if (!bluetoothAdapter.isEnabled()) {
            //TODO: Handle Bluetooth being off
            SetBtStatus(false, false, false);
            return;
        }

        try {
            //Get Bluetooth device
            BluetoothDevice btDevice = bluetoothAdapter.getRemoteDevice("B0:B4:48:9E:5B:4A"); //TODO: Have a custom address

            //Create Pebble device
            PebbleWatchDevice pbl = new PebbleWatchDevice(this);

            //Create transport
            BluetoothTransport transport = new BluetoothTransport(btDevice, pbl);
            pbl.transport = transport;

            //Open transport
            transport.OpenConnection();
            pblDevice = pbl;

            //Tell the device that we're ready
            pbl.OnConnectionOpened();

            //Log OK
            RpwsLog.Log("background-bt", "Opened connection to Pebble smartwatch OK!");
            SetBtStatus(true, false, true);
        } catch (Exception ex) {
            RpwsLog.Log("background-bt-error", "Failed to start background service with error "+ex.toString()+"!");
            SetBtStatus(false, false, true);
        }
    }

    public void SetBtStatus(Boolean connected, Boolean retrying, Boolean isBtOn) {
        if(bgNotif != null) {
            //Build notification
            NotificationCompat.Builder builder = GetDefaultNotif();
            if(!isBtOn){
                //Show that the BT radio is off
                builder.setContentTitle(getString(R.string.persist_notification_title_off));
                builder.setContentText(getString(R.string.persist_notification_body_off));
            } else {
                if(connected) {
                    builder.setContentTitle(getString(R.string.persist_notification_title_connected));
                } else {
                    builder.setContentTitle(getString(R.string.persist_notification_title_lost));
                }
            }

            //Show notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Notification n = builder.build();
            bgNotif = n;
            notificationManager.notify(1, n);
        }
    }
}
