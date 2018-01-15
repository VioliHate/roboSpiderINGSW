package com.ingsw2017.unical.mat.robospider;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //Found, add to a device list
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.handleBluetoothButton)).setText(bluetoothAdapter.isEnabled()?"Disable Bluetooth":"Enable Bluetooth");
    }

    public void handleBluetooth(View v)
    {
        //Enable bluetooth
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            ((Button) findViewById(R.id.handleBluetoothButton)).setText("Disable Bluetooth");
            //dialog avvenuto collegamento
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Bluetooth is enabled")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
        //disable bluetooth
        else
        {
            bluetoothAdapter.disable();
            ((Button) findViewById(R.id.handleBluetoothButton)).setText("Enable Bluetooth");
            //dialog avvenuto collegamento
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Bluetooth is disabled")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }

    public void connect(View v)
    {
        //check if bluetooth is supported
        if(bluetoothAdapter==null)
        {
            Toast.makeText(this,"Bluetooth not supported!",Toast.LENGTH_SHORT).show();
        }
        else if(!bluetoothAdapter.isEnabled())
        {
            //dialog attivazione bluetooth
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Bluetooth request")
                    .setMessage("Robospider is requesting to turn on Bluetooth. Allow?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            bluetoothAdapter.enable();
                            ((Button) findViewById(R.id.handleBluetoothButton)).setText("Disable Bluetooth");
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
            Intent startNewActivity=new Intent(this, BluetoothHandlerActivity.class);
            startActivity(startNewActivity);
        }
    }

    public void exit(View v)
    {
        if(bluetoothAdapter.isEnabled()) {
        //dialog disattivazione bluetooth prima di uscire
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Bluetooth is enabled")
                .setMessage("Do you want to turn it down before exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bluetoothAdapter.disable();
                        ((Button) findViewById(R.id.handleBluetoothButton)).setText("Enable Bluetooth");
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }

        //SOLUZIONE PIù PULITA PER CHIUDERE L'APP
//        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory( Intent.CATEGORY_HOME );
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
    }
}
