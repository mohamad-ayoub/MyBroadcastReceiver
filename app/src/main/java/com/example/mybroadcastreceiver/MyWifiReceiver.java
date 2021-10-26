package com.example.mybroadcastreceiver;

import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class MyWifiReceiver extends BroadcastReceiver {
    private static final String TAG = MyWifiReceiver.class.getSimpleName();
    private static final String[] actions = {WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION};

    private Context context;
    private boolean isRegisterd;


    public MyWifiReceiver() {
        this.context = null;
        isRegisterd = false;
    }
       public MyWifiReceiver(Context context) {
        this.context = context;
        isRegisterd = false;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            default:
                Toast.makeText(context, "Unhandled wifi action: " + intent.getAction().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void register() {
        if (!isRegisterd) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(actions[0]);

            context.registerReceiver(this, intentFilter);
            Toast.makeText(context, TAG + " : registerd", Toast.LENGTH_SHORT).show();
            isRegisterd = true;
        }
    }
    public void unRregister() {
        if (isRegisterd) {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, TAG + " : unRegisterd", Toast.LENGTH_SHORT).show();
            isRegisterd = false;
        }
    }

    public boolean isRegisterd() {
        return isRegisterd;
    }

}