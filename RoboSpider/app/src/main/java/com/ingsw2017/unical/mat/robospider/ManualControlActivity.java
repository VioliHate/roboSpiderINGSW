package com.ingsw2017.unical.mat.robospider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class ManualControlActivity extends AppCompatActivity {

    JoystickController joystickController;
    RelativeLayout layout_joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        joystickController=new JoystickController(this,layout_joystick,R.drawable.joystick_ball);
        joystickController.setOffset(90);
        joystickController.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                joystickController.drawStick(arg1);
                int direction = joystickController.get8Direction();
                if (direction == JoystickController.STICK_UP) {
                    System.out.println("Direction : Up");
                } else if (direction == JoystickController.STICK_UPRIGHT) {
                    System.out.println("Direction : Up Right");
                } else if (direction == JoystickController.STICK_RIGHT) {
                    System.out.println("Direction : Right");
                } else if (direction == JoystickController.STICK_DOWNRIGHT) {
                    System.out.println("Direction : Down Right");
                } else if (direction == JoystickController.STICK_DOWN) {
                    System.out.println("Direction : Down");
                } else if (direction == JoystickController.STICK_DOWNLEFT) {
                    System.out.println("Direction : Down Left");
                } else if (direction == JoystickController.STICK_LEFT) {
                    System.out.println("Direction : Left");
                } else if (direction == JoystickController.STICK_UPLEFT) {
                    System.out.println("Direction : Up Left");
                } else if (direction == JoystickController.STICK_NONE) {
                    System.out.println("Direction : Center");
                }
                return true;
            }
        });
    }
}
