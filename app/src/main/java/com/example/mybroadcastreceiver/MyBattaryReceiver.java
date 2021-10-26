package com.example.mybroadcastreceiver;

import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class MyBattaryReceiver extends BroadcastReceiver {
    private static final String TAG = MyBattaryReceiver.class.getSimpleName();
    private static final String[] actions = {ACTION_BATTERY_LOW, ACTION_BATTERY_OKAY};

    private Context context;
    private boolean isRegisterd;


    public MyBattaryReceiver() {
        this.context = null;
        isRegisterd = false;
    }

    public MyBattaryReceiver(Context context) {
        this.context = context;
        isRegisterd = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_BATTERY_LOW:
                Toast.makeText(context, "Battary Low! please charge!", Toast.LENGTH_SHORT).show();
                break;
            case ACTION_BATTERY_OKAY:
                Toast.makeText(context, "Battary Okay", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Unhandled battary action: " + intent.getAction().toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void register() {
        if (!isRegisterd) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(actions[0]);
            intentFilter.addAction(actions[1]);

            context.registerReceiver(this, intentFilter);
            Toast.makeText(context, TAG + " : registerd", Toast.LENGTH_SHORT).show();
            isRegisterd = true;
        }
    }

    public void unRegister() {
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