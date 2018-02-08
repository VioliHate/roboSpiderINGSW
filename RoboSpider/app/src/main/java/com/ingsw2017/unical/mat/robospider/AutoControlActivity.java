package com.ingsw2017.unical.mat.robospider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class AutoControlActivity extends AppCompatActivity {

    private Switch switchAutoControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_control);
        switchAutoControl=(Switch) findViewById(R.id.switchAutoControl);

        switchAutoControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    ((Switch) findViewById(R.id.switchAutoControl)).setTextColor(Color.GREEN);
                    ((Switch) findViewById(R.id.switchAutoControl)).setText("ON");
                    BluetoothHandlerActivity.getClassInstance().sendMessage("a");
                }
                else
                {
                    ((Switch) findViewById(R.id.switchAutoControl)).setTextColor(0xFFFF4444);
                    ((Switch) findViewById(R.id.switchAutoControl)).setText("OFF");
                    BluetoothHandlerActivity.getClassInstance().sendMessage("s");
                }
            }
        });
    }
}
