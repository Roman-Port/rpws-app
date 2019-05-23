package com.romanport.rpws.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.romanport.rpws.R;
import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.bluetooth.BluetoothTransport;

public class PebbleBackgroundService extends Service {

    public PebbleDevice pblDevice;
    public Notification bgNotif;

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
            PebbleWatchDevice pbl = new PebbleWatchDevice();

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
