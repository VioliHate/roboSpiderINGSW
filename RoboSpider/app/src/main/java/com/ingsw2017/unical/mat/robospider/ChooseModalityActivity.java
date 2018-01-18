package com.ingsw2017.unical.mat.robospider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ingsw2017.unical.mat.robospider.R;

public class ChooseModalityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_modality);
    }

    public void autoControl(View v)
    {
        Intent startNewActivity=new Intent(this, AutoControlActivity.class);
        startActivity(startNewActivity);
    }

    public void manualControl(View v)
    {
        Intent startNewActivity=new Intent(this, ManualControlActivity.class);
        startActivity(startNewActivity);
    }
}
