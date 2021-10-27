package com.example.mybroadcastreceiver;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    MySmsReceiver sms;
    MyWifiReceiver wifi;
    MyBattaryReceiver battary;
    ToggleButton tglWifi, tglSms, tglBattary;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("NOTIFICATION_ID")) {
            int id = getIntent().getExtras().getInt("NOTIFICATION_ID");
            Toast.makeText(this, "activated by notification", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Notification Id:  " + id, Toast.LENGTH_LONG).show();
        }

        wifi = new MyWifiReceiver(this);
        sms = new MySmsReceiver(this);
        battary = new MyBattaryReceiver(this);

        tglWifi = findViewById(R.id.tglWifi);
        tglBattary = findViewById(R.id.tglLowBattary);
        tglSms = findViewById(R.id.tglSms);

        tglWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifi.register();
                } else {
                    wifi.unRregister();
                }

                buttonView.setChecked(wifi.isRegisterd());
            }
        });


        tglBattary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!battary.isRegisterd()) {
                        battary.register();
                    }
                } else {
                    if (battary.isRegisterd()) {
                        battary.unRegister();
                    }
                }

                buttonView.setChecked(battary.isRegisterd());
            }
        });


        tglSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sms.register();
                } else {
                    sms.unRegister();
                }

                buttonView.setChecked(sms.isRegisterd());
            }
        });

        tglWifi.setChecked(wifi.isRegisterd());
        tglBattary.setChecked(battary.isRegisterd());
        tglSms.setChecked(sms.isRegisterd());

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (((ToggleButton) findViewById(R.id.tglWifi)).isChecked()) {
            wifi.register();
        }
        if (((ToggleButton) findViewById(R.id.tglLowBattary)).isChecked()) {
            battary.register();
        }

        if (((ToggleButton) findViewById(R.id.tglSms)).isChecked()) {
            sms.register();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifi.unRregister();
        battary.unRegister();
        sms.unRegister();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setAlarm(View view) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));  // set hour
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));          // set minute
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND)+5);               // set seconds

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
        finish();
    }
}