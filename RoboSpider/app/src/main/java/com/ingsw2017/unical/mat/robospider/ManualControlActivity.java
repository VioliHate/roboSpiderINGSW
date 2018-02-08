package com.ingsw2017.unical.mat.robospider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ManualControlActivity extends AppCompatActivity {

    private ImageView imageButtonUp;
    private ImageView imageButtonDown;
    private ImageView imageButtonRight;
    private ImageView imageButtonLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);

        imageButtonUp=(ImageView) findViewById(R.id.imageButtonUp);
        imageButtonDown=(ImageView) findViewById(R.id.imageButtonDown);
        imageButtonRight=(ImageView) findViewById(R.id.imageButtonRight);
        imageButtonLeft=(ImageView) findViewById(R.id.imageButtonLeft);
    }

    public void sit(View view)
    {
        BluetoothHandlerActivity.getClassInstance().sendMessage("2");
        System.out.println("Action : 2");
    }

    public void stand(View view)
    {
        BluetoothHandlerActivity.getClassInstance().sendMessage("c");
        System.out.println("Action : c");
    }

    public void sayHi(View view)
    {
        BluetoothHandlerActivity.getClassInstance().sendMessage("1");
        System.out.println("Action : 1");
    }

    public void upButton(View view) {
        BluetoothHandlerActivity.getClassInstance().sendMessage("u");
        System.out.println("Direction : Up");
    }

    public void leftButton(View view) {
        BluetoothHandlerActivity.getClassInstance().sendMessage("l");
        System.out.println("Direction : Left");
    }

    public void rightButton(View view) {
        BluetoothHandlerActivity.getClassInstance().sendMessage("r");
        System.out.println("Direction : Right");
    }

    public void downButton(View view) {
        BluetoothHandlerActivity.getClassInstance().sendMessage("d");
        System.out.println("Direction : Down");
    }
}
