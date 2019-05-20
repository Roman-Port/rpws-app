package com.romanport.rpws;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.romanport.rpws.device.PebbleBackgroundService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RpwsLog.Log("debug", "Trying to start service...");
        //startForegroundService(new Intent(this, PebbleBackgroundService.class));
        PebbleBackgroundService.StartBgService(this);
    }
}
