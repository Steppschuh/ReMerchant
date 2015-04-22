package com.steppschuh.remerchant;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MobileApp extends Application {

    public static String TAG = "RM";

    private boolean isInitialized = false;
    private Activity contextActivity;

    BroadcastReceiver mReceiver;
    ArrayList<String> visibleDevices;

    public void initialize(Activity contextActivity) {
        this.contextActivity = contextActivity;

        startBluetoothDiscovery();

        isInitialized = true;
    }

    public void scanForDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        List<String> s = new ArrayList<String>();
        for(BluetoothDevice bt : pairedDevices) {
            s.add(bt.getName());
            Log.d(TAG, "Paired device: " + bt.getName());
        }

    }

    public void startBluetoothDiscovery() {
        Log.d(TAG, "Starting device discovery");
        visibleDevices = new ArrayList<>();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();

        // Create a BroadcastReceiver for ACTION_FOUND
        final List<String> discoverableDevicesList = new ArrayList<String>();

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    // Add the name and address to an array adapter to show in a ListView

                    Log.d(TAG, "Device found: " + device.getName() + " - " + device.getAddress());
                    discoverableDevicesList.add(device.getName() + "\n" + device.getAddress() + "\n" + rssi);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Log.d(TAG, "Device discovery started");
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Log.d(TAG, "Device discovery finished");
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        contextActivity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    public void destroy() {
        contextActivity.unregisterReceiver(mReceiver);
    }

    /**
     * Getter & Setter
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    public Activity getContextActivity() {
        return contextActivity;
    }

    public void setContextActivity(Activity contextActivity) {
        this.contextActivity = contextActivity;
    }
}
