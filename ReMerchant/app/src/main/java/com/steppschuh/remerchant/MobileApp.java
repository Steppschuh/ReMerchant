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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MobileApp extends Application {

    public static String TAG = "RM";

    private boolean isInitialized = false;

    private boolean shouldRunDeviceDetection = false;
    private long lastDeviceDetection = 0;
    private long lastBluetoothBroadcast = 0;
    private long lastBluetoothRestart = 0;
    public static final int DEVICE_DETECTION_INTERVAL = 10000;
    public static final int BLUETOOTH_BROADCAST_INTERVAL = 5000;
    public static final int BLUETOOTH_RESTART_INTERVAL = 30000;

    private Activity contextActivity;

    BroadcastReceiver mReceiver;
    ArrayList<Customer> customers;

    public void initialize(Activity contextActivity) {
        this.contextActivity = contextActivity;

        restoreSampleCustomers();
        startDeviceDetection();

        invokeBluetoothBoradcast();
        scanForDevices();

        isInitialized = true;
    }


    private void restoreSampleCustomers() {
        customers = new ArrayList<>();

        Customer customer1 = new Customer(0);
        customer1.setName("John Doe");
        customer1.setLoyality(2);
        customer1.setDeviceId("78:A5:04:17:5C:F7");
        customer1.setDeviceName("78A504175CF7");
        customer1.setLastVisit(0);
        customer1.setPicture(getResources().getDrawable(R.drawable.sample_profile));

        Customer customer2 = new Customer(1);
        customer2.setName("Jane Doe");
        customer2.setLoyality(4);
        customer2.setDeviceId("78:A5:04:17:5D:84");
        customer2.setDeviceName("78A504175D84");
        customer2.setLastVisit(0);
        customer2.setPicture(getResources().getDrawable(R.drawable.sample_profile));

        Customer customer3 = new Customer(1);
        customer3.setName("Max Mustermann");
        customer3.setLoyality(4);
        customer3.setDeviceId("78:A5:04:17:60:2A");
        customer3.setDeviceName("78A50417602A");
        customer3.setLastVisit(0);
        customer3.setPicture(getResources().getDrawable(R.drawable.sample_profile));

        Customer customer4 = new Customer(1);
        customer4.setName("Marie Mustermann");
        customer4.setLoyality(10);
        customer4.setDeviceId("10:A5:04:17:5D:84");
        customer4.setDeviceName("10A504175D84");
        customer4.setLastVisit(0);
        customer4.setPicture(getResources().getDrawable(R.drawable.sample_profile));

        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
        customers.add(customer4);

        Log.d(TAG, "Known customers:");
        for (Customer customer : customers) {
            Log.d(TAG, " - " + customer.getName() + ": " + customer.getDevice());
        }
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

    public void startDeviceDetection() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();

        mReceiver = new bluetoothBroadcastReceiver();

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        //this.registerReceiver(mReceiver, filter);
        contextActivity.registerReceiver(mReceiver, filter);

        shouldRunDeviceDetection = true;
        runDeviceDetection();
    }

    public void stopDeviceDetection() {
        shouldRunDeviceDetection = false;
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothAdapter.cancelDiscovery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void runDeviceDetection() {
        (new Thread() {
            @Override
            public void run() {
                do {
                    try {
                        long now = (new Date()).getTime();
                        if (lastBluetoothRestart + BLUETOOTH_RESTART_INTERVAL < now) {
                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            mBluetoothAdapter.disable();
                            lastBluetoothRestart = now;
                        }
                        if (lastBluetoothBroadcast + BLUETOOTH_BROADCAST_INTERVAL < now) {
                            invokeBluetoothBoradcast();
                        }

                        if (lastDeviceDetection + DEVICE_DETECTION_INTERVAL < now) {
                            searchForKnownDevices();
                        } else {
                            sleep(500);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } while (shouldRunDeviceDetection);
            }
        }).start();
    }

    private void invokeBluetoothBoradcast() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            //Log.d(TAG, "Bluetooth adapter enabled");
        }

        if (mBluetoothAdapter.isDiscovering()) {
            //mBluetoothAdapter.cancelDiscovery();
            //Log.d(TAG, "Restarting device discovery");
        } else {
            mBluetoothAdapter.startDiscovery();
        }

        Log.d(TAG, "Bluetooth broadcast invoked");

        lastBluetoothBroadcast = (new Date()).getTime();
    }

    private void searchForKnownDevices() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        Log.d(TAG, "Scanning for customers:");
        for (Customer customer : customers) {
            try {
                //Log.d(TAG, "Scanning for customer: " + customer.getDevice());
                BluetoothDevice customerDevice = mBluetoothAdapter.getRemoteDevice(customer.getDeviceId());
                if (customerDevice.getName() == null) {
                    throw new Exception("customerDevice is null");
                } else {
                    //Log.d(TAG, customer.getName() + " near by!");
                    customer.setLastVisit((new Date()).getTime());
                }
            } catch (Exception ex) {
              //Log.d(TAG, ex.getMessage());
            }

            if (customer.recentlySeen()) {
                Log.i(TAG, " - " + customer.getName() + " last seen: " + String.valueOf(customer.getLastSeenDelta()) + " seconds ago");
            } else {
                Log.d(TAG, " - " + customer.getName() + " hasn't been around for a while");
            }
        }

        lastDeviceDetection = (new Date()).getTime();
    }

    public Customer getCustomerByDevice(String device) {
        if (device == null) {
            return null;
        }
        for (Customer customer : customers) {
            if (customer.getDevice().equals(device)) {
                return customer;
            }
        }
        return null;
    }

    private class bluetoothBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                String deviceString = Customer.getDevice(device.getAddress(), device.getName());
                Customer customer = getCustomerByDevice(deviceString);

                if (customer != null) {
                    customer.setLastVisit((new Date()).getTime());
                    customer.setRssi(rssi);

                    Log.d(TAG, " - New customer Blukii identified: name: " + customer.getName() + " device: " + customer.getDevice());
                } else {
                    if (device.getName() != null && device.getName().contains("blukii")) {
                        Log.d(TAG, " - New blukii found: " + deviceString + " > name: " + device.getName() + " id: " + device.getAddress() + " rssi: " + String.valueOf(rssi));
                    } else {
                        //Log.d(TAG, " - Unknown device: " + deviceString + " > name: " + device.getName() + " id: " + device.getAddress() + " rssi: " + String.valueOf(rssi));
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //Log.d(TAG, "Device discovery started");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Log.d(TAG, "Device discovery finished");
            } else {
                //Log.d(TAG, "Broadcast action: " + action);
            }
        }
    }

    public void destroy() {
        try {
            stopDeviceDetection();
            contextActivity.unregisterReceiver(mReceiver);
        } catch (Exception ex) {

        }
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

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}
