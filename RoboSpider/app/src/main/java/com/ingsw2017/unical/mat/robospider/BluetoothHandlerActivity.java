package com.ingsw2017.unical.mat.robospider;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothHandlerActivity extends AppCompatActivity {

    private Button discoverableButton,pairedButton,searchButton,connectRoboSpiderButton;
    private ListView listPaired;
    private ListView listAllDevices;

    private static final int REQUEST_ENABLED=0;
    private static final int REQUEST_DISCOVERABLE=0;
    final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    BluetoothAdapter bluetoothAdapter;
    //list for all devices;
    ArrayList<String> devices;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device.getName() + "\n" + device.getAddress());
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                ArrayAdapter arrayAdapter = new ArrayAdapter(BluetoothHandlerActivity.this, R.layout.device_text_style, devices);
                listAllDevices.setAdapter(arrayAdapter);
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
        connectRoboSpiderButton=(Button) findViewById(R.id.connectRoboSpiderButton);

        listPaired= (ListView) findViewById(R.id.listPaired);
        listAllDevices= (ListView) findViewById(R.id.listAllDevices);

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //list all visible bluetooth devices
        registerReceiver(broadcastReceiver, filter);

        searchPairedDevices();
        searchAllDevices();

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
                searchPairedDevices();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAllDevices();
            }
        });

        connectRoboSpiderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity=new Intent(BluetoothHandlerActivity.this, ChooseModalityActivity.class);
                startActivity(startNewActivity);
            }
        });

        listPaired.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                String deviceAddress= ((String) listPaired.getItemAtPosition(position)).split("\\r?\\n")[1];
                BluetoothDevice selectedDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

                try {
                    selectedDevice.getClass().getMethod("removeBond", (Class[]) null).invoke(selectedDevice, (Object[]) null);
                    Log.i("Log", "Removed"+deviceAddress);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Log.i("Log", "NOT Removed"+deviceAddress);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.i("Log", "NOT Removed"+deviceAddress);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    Log.i("Log", "NOT Removed"+deviceAddress);
                }
                searchPairedDevices();
            }
        });

        listAllDevices.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                String deviceAddress= ((String) listAllDevices.getItemAtPosition(position)).split("\\r?\\n")[1];
                BluetoothDevice selectedDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
                Log.i("Log", "The dvice : "+selectedDevice.toString());
                Boolean isBonded = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    isBonded = selectedDevice.createBond();
                }
                if(isBonded)
                {
                    Log.i("Log", "The bond is created: "+isBonded);
                }
                else
                {
                    Log.i("Log", "The bond is NOT created: "+isBonded);
                }

            }
        });

    }

    private void searchPairedDevices()
    {
        //list paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        ArrayList<String> devices = new ArrayList<>();

        for (BluetoothDevice bluetoothDevice : pairedDevices) {
            devices.add(bluetoothDevice.getName()+ "\n" + bluetoothDevice.getAddress());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(BluetoothHandlerActivity.this,R.layout.paired_device_text_style, devices);

        listPaired.setAdapter(arrayAdapter);
    }

    private void searchAllDevices()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
            switch (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                case PackageManager.PERMISSION_DENIED:
                    ((TextView) new AlertDialog.Builder(BluetoothHandlerActivity.this)
                            .setTitle("Runtime Permissions up ahead")
                            .setMessage(Html.fromHtml("<p>To find nearby bluetooth devices please click \"Allow\" on the runtime permissions popup.</p>" +
                                    "<p>For more info see <a href=\"http://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id\">here</a>.</p>"))
                            .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(BluetoothHandlerActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                                    }
                                }
                            })
                            .show()
                            .findViewById(android.R.id.message))
                            .setMovementMethod(LinkMovementMethod.getInstance());       // Make the link clickable. Needs to be called after show(), in order to generate hyperlinks
                    break;
                case PackageManager.PERMISSION_GRANTED:
                    break;
            }
        }
        //init list for broadcast receiver
        devices=new ArrayList<>();
        //start to find all bluetooth device, spend around 12 seconds
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


}
