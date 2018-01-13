package com.ingsw2017.unical.mat.robospider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothHandlerActivity extends AppCompatActivity {

    Button discoverableButton,pairedButton,searchButton;
    ListView listPaired;
    ListView listAllDevices;

    private static final int REQUEST_ENABLED=0;
    private static final int REQUEST_DISCOVERABLE=0;

    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ArrayList<String> devices = new ArrayList<>();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                listAllDevices.setAdapter(new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, devices));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_handler);

        discoverableButton=(Button) findViewById(R.id.discoverableButton);
        pairedButton=(Button) findViewById(R.id.pairedButton);
        searchButton=(Button) findViewById(R.id.searchButton);

        listPaired= (ListView) findViewById(R.id.listPaired);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        //start to find all bluetooth device, spend around 12 seconds
        bluetoothAdapter.startDiscovery();
        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //list all visible bluetooth devices
        //registerReceiver(broadcastReceiver, filter);
        registerReceiver(broadcastReceiver, bluetoothFilter);

        if (bluetoothAdapter.isDiscovering()) {
            // cancel the discovery if it has already started
            bluetoothAdapter.cancelDiscovery();
            System.out.println("DISCOVERY CANCEL");
        }

        if (bluetoothAdapter.startDiscovery()) {
            // bluetooth has started discovery
            System.out.println("DISCOVERY STARTED");
        }

        discoverableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make the device discoverable
                if(!bluetoothAdapter.isDiscovering())
                {Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent,REQUEST_ENABLED);}
            }
        });

        pairedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list paired devices
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                ArrayList<String> devices = new ArrayList<>();

                for (BluetoothDevice bluetoothDevice : pairedDevices) {
                    devices.add(bluetoothDevice.getName());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(BluetoothHandlerActivity.this, android.R.layout.simple_list_item_1, devices);

                listPaired.setAdapter(arrayAdapter);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start to find all bluetooth device, spend around 12 seconds
                bluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                //list all visible bluetooth devices
                registerReceiver(broadcastReceiver, filter);
            }
        });


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


}
