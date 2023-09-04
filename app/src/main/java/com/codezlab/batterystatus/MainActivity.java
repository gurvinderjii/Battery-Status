package com.codezlab.batterystatus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    TextView show, isCharged;
    LottieAnimationView lottie;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);

            if (status == BatteryManager.BATTERY_STATUS_CHARGING &&  !(status == BatteryManager.BATTERY_STATUS_FULL)){
                lottie.playAnimation();
            } else if (status == BatteryManager.BATTERY_STATUS_FULL){
                lottie.cancelAnimation();
                isCharged.setVisibility(View.VISIBLE);
            }

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
                lottie.playAnimation();
            } else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
                lottie.cancelAnimation();
            }
            show.setText(String.valueOf(level) + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = findViewById(R.id.showPercentage);
        lottie = findViewById(R.id.lottie);
        isCharged = findViewById(R.id.charged);

    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(broadcastReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}