package com.example.martin.speechtotext;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Main_Bluetooth extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    ListView listDevicesFound;
    Button btnScanDevice;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;
    ListItemClicked listItemClicked;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    BluetoothDevice bdDevice;
    ArrayList<String> arrayListNameAndAddress = null;
    String deviceString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__bluetooth);

        btnScanDevice = (Button)findViewById(R.id.scandevice);

        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        arrayListBluetoothDevices = new ArrayList<>();
        arrayListNameAndAddress = new ArrayList<>();
        listItemClicked = new ListItemClicked();

        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<>(Main_Bluetooth.this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        btArrayAdapter.notifyDataSetChanged();

        CheckBlueToothState();

        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

        registerReceiver(ActionFoundReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        listDevicesFound.setOnItemClickListener(listItemClicked);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);
        finish();
    }

    private void CheckBlueToothState(){
        if (bluetoothAdapter == null){
            stateBluetooth.setText("Bluetooth NOT support");
        }else{
            if (bluetoothAdapter.isEnabled()){
                if(bluetoothAdapter.isDiscovering()){
                    stateBluetooth.setText("Bluetooth is currently in device discovery process.");
                }else{
                    stateBluetooth.setText("Bluetooth is Enabled.");
                    btnScanDevice.setEnabled(true);
                }
            }else{
                stateBluetooth.setText("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private Button.OnClickListener btnScanDeviceOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btArrayAdapter.clear();
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
        }};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_ENABLE_BT){
            CheckBlueToothState();
        }
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                arrayListBluetoothDevices.add(device);
                arrayListNameAndAddress.add(device.getName() + " " + device.getAddress());
                btArrayAdapter.notifyDataSetChanged();

            }
        }};

    class ListItemClicked implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            bdDevice = arrayListBluetoothDevices.get(position);
            deviceString = arrayListNameAndAddress.get(position);

            String[] nameAndAddress = deviceString.split(" ");

            bdDevice.createBond();

            Log.i("Log", "The device : " + nameAndAddress[0] + " [" + nameAndAddress[1] + "]");
            Intent _result = new Intent();
            _result.setData(Uri.parse(nameAndAddress[0]));
            setResult(RESULT_OK, _result);
            finish();
        }
    }
}

