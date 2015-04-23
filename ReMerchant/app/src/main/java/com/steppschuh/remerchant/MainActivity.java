package com.steppschuh.remerchant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    MobileApp app;
    public static final int CAMERA_REQUEST = 5;
    public static final int PAYMENT_REQUEST = 6;

    Fragment lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(MobileApp.TAG, "Creating main activity");

        app = (MobileApp) getApplication();
        if (!app.isInitialized()) {
            app.initialize(this);
        } else {
            app.setContextActivity(this);
        }

        showCustomerList();
    }

    public void showCustomerDetail(String device) {
        FragmentCustomerDetail fragment = new FragmentCustomerDetail();
        fragment.setCustomerDevice(device);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.container, fragment)
                .commit();

        lastFragment = fragment;
    }

    public void addNewCustomer(String device) {
        FragmentCustomerNew fragment = new FragmentCustomerNew();
        fragment.setCustomerDevice(device);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.container, fragment)
                .commit();

        lastFragment = fragment;
    }

    public void showCustomerList() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new FragmentCustomersPager())
                .commit();

        lastFragment = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(MobileApp.TAG, "onActivityResult: " + requestCode + " result: " + resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ((FragmentCustomerNew) lastFragment).updatePhoto(new BitmapDrawable(getResources(), photo));
                Log.d(MobileApp.TAG, "Customer photo updated");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.destroy();
    }
}
