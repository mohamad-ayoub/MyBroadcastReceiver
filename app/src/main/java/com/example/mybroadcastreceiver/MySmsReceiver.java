package com.example.mybroadcastreceiver;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;
import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class MySmsReceiver extends BroadcastReceiver {
    private static final String TAG = BroadcastReceiver.class.getSimpleName();
    private static final String[] actions = {SMS_RECEIVED_ACTION, GET_ACCOUNTS};
    public static final String pdu_type = "pdus";
    private static final String CHANAL_ID = "Chanal:" + TAG;


    private Context context;
    private boolean isRegisterd;

    public MySmsReceiver() {
        ;
        isRegisterd = false;
    }

    public MySmsReceiver(Context context) {
        this.context = context;
        isRegisterd = true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";

        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {

                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " : " + msgs[i].getMessageBody() + "\n";

                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
                createAndShowSMSNotification(context, msgs[i].getOriginatingAddress(), msgs[i].getMessageBody());
            }
        }
    }


    private void createAndShowSMSNotification(Context context, String phone, String message) {
        NotificationChannel channel1 = null;
        NotificationManager manager = null;
        Log.d(TAG, "createAndShowSMSNotification: ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(CHANAL_ID,
                    "channel: " + TAG,
                    NotificationManager.IMPORTANCE_HIGH);
            // create the settings as default user can change
            // user can see in the settings
            channel1.setDescription(" Channel 1 user for...");


            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }


        Intent notifyIntent = new Intent(context, MainActivity.class);
        int notificationId = Integer.parseInt(phone.substring(phone.length()-8,phone.length()-1));
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Bundle extras = new Bundle();
        extras.putInt("NOTIFICATION_ID",notificationId);
        extras.putCharArray("NOTIFICATION_PHONE_NO",phone.toCharArray());
        extras.putInt("NOTIFICATION_BODY",333333);
        notifyIntent.putExtras(extras);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context, notificationId, notifyIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, CHANAL_ID).
                setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("sms from " + phone)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(notificationId, notification);
    }

    public void register() {
        // registered in manifest.xml.xml
        /*
        if (!isRegisterd) {
            if (true) { //(checkIfAlreadyhavePermission()) {
                IntentFilter intentFilter = new IntentFilter();
                for (int i = 0; i < actions.length; i++) {
                    intentFilter.addAction(actions[i]);
                }
                context.registerReceiver(this, intentFilter);

                Toast.makeText(context, TAG + " : registerd", Toast.LENGTH_SHORT).show();
                isRegisterd = true;
            }
        }
         */
    }

    public void unRegister() {
        /*
        if (isRegisterd) {
            int MyVersion = Build.VERSION.SDK_INT;
            // if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            //     if (!checkIfAlreadyhavePermission()) {
            //         return;
            //     }
            //}
            try {
                context.unregisterReceiver(this);
                Toast.makeText(context, TAG + " : UNregisterd", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isRegisterd = false;
        }
         */
    }

    public boolean isRegisterd() {
        return isRegisterd;
    }
}


