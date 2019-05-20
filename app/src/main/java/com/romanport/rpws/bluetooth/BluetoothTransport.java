package com.romanport.rpws.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Base64;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.device.PebbleTransport;
import com.romanport.rpws.device.PebbleTransportInterface;
import com.romanport.rpws.util.ByteDecoder;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class BluetoothTransport extends PebbleTransport {

    public BluetoothDevice btDevice;
    public BluetoothSocket btSock;
    public InputStream btInStream;
    public OutputStream btOutStream;
    public Thread workerThread;

    public BluetoothTransport(BluetoothDevice btDevice, PebbleTransportInterface i) {
        this.outputInterface = i;
        this.btDevice = btDevice;
    }

    @Override
    public void OpenConnection() throws Exception {
        //Open socket to device using the UUID of the official Pebble app
        BluetoothSocket sock = btDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        sock.connect();
        btSock = sock;

        //We've connected. Grab streams
        btInStream = btSock.getInputStream();
        btOutStream = btSock.getOutputStream();

        //Start background worker
        StartBgWorkerThread();
    }

    public void StartBgWorkerThread() {
        workerThread = new Thread(new Runnable() {
            public void run() {

                try {
                    //Begin listening for Pebble BT messages
                    while(true) {
                        //Read length
                        byte[] buffer = new byte[2];
                        int bytesRead = btInStream.read(buffer, 0, 2);

                        //Now, read full packet
                        int len = ByteDecoder.DecodeShort(buffer) + 2;
                        buffer = new byte[len];
                        bytesRead = btInStream.read(buffer, 0, len);

                        //Actually use this data
                        RpwsLog.Log("pbl-device-bg-rx", "Read "+bytesRead+" bytes: "+Base64.encodeToString(buffer, Base64.NO_WRAP));
                        outputInterface.OnGetMsg(buffer);
                    }
                } catch (Exception ex){
                    //TODO: Handle error here.
                    RpwsLog.LogException("pbl-device-bg-err", "Fatal error in background worker thread: ",ex);
                }

            }
        });
        workerThread.start();
    }

    @Override
    public void CloseConnection() {

    }

    @Override
    public void SendMsg(byte[] msg) {
        try {
            btOutStream.write(msg);
            RpwsLog.Log("pbl-device-bg-tx", "Wrote "+msg.length+" bytes: "+ Base64.encodeToString(msg, Base64.NO_WRAP));
        } catch (Exception ex) {
            RpwsLog.Log("pbl-device-bg-err", "Error sending BT packet: "+ex.toString());
        }
    }
}
