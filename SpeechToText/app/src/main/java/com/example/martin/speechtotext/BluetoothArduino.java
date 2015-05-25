package com.example.martin.speechtotext;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

public class BluetoothArduino extends Thread {
    private BluetoothAdapter mBlueAdapter = null;
    private BluetoothSocket mBlueSocket = null;
    private BluetoothDevice mBlueRobo = null;

    OutputStream mOut;
    InputStream mIn;
    private boolean robotFound = false;
    private boolean connected = false;
    private String robotName;
    private List<String> mMessages = new ArrayList<String>();
    private String TAG = "BluetoothConnector";

    public BluetoothArduino(String Name){
        try {
            for(int i = 0; i < 2048; i++){
                mMessages.add("");
            }
            robotName = Name;
            mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBlueAdapter == null) {
                LogError("\t\t[#]Phone does not support bluetooth!!");
                return;
            }
            if (!isBluetoothEnabled()) {
              LogError("[#]Bluetooth is not activated!!");
            }

            Set<BluetoothDevice> paired = mBlueAdapter.getBondedDevices();
            if (paired.size() > 0) {
                for (BluetoothDevice d : paired) {
                    if (d.getName().equals(robotName)) {
                        mBlueRobo = d;
                        robotFound = true;
                        break;
                    }
                }
            }
            
            if (!robotFound)
                LogError("\t\t[#]There is no device paired!!");

        }catch (Exception e){
            LogError("\t\t[#]Error creating Bluetooth! : " + e.getMessage());
        }

    }

    public boolean isBluetoothEnabled(){
      return mBlueAdapter.isEnabled();
    }

    public boolean Connect(){
        if(!robotFound)
            return false;
        try{
            LogMessage("\t\tConnecting to the robot...");

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mBlueSocket = mBlueRobo.createRfcommSocketToServiceRecord(uuid);
            if(!mBlueSocket.isConnected()) {
                Thread.sleep(300);
                mBlueSocket.connect();
            }
            mOut = mBlueSocket.getOutputStream();
            mIn = mBlueSocket.getInputStream();
            connected = true;
            this.start();
            LogMessage("\t\t\t" + mBlueAdapter.getName());
            LogMessage("\t\tOk!!");
            return true;

        }catch (Exception e){
            LogError("\t\t[#]Error while connecting: " + e.getMessage());
            return false;
        }
    }

    public void sendMessage(String msg){
        try {
            if(connected) {
                mOut.write(msg.getBytes());
            }
        } catch (IOException e){
            LogError("->[#]Error while sending message: " + e.getMessage());
        }
    }
    
    private void LogMessage(String msg){
      Log.d(TAG, msg);
    }
    
    private void LogError(String msg){
      Log.e(TAG, msg);
    }

    public void Disconnect(String name){
        try {
            mIn.close();
            mOut.close();
            mBlueSocket.close();
            Log.i("DC", "Disconnected from: " + name);
        }catch (Exception e){
            Log.i("DC", "Couldn't disconnect from: " + name);
        }
    }

}