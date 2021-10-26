package com.example.mybroadcastreceiver;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    MySmsReceiver sms;
    MyWifiReceiver wifi;
    MyBattaryReceiver battary;
    ToggleButton tglWifi, tglSms, tglBattary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        Toast.makeText(this, "git@github.com:mohamad-ayoub/MyBroadcastReceiver.git", Toast.LENGTH_SHORT).show();
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
}