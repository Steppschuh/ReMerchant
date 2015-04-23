package com.steppschuh.remerchant;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

public class FragmentCustomerNew extends Fragment {

    MobileApp app;

    View contentFragment;
    TextView customerName;
    TextView customerDeviceName;
    ImageView customerImage;
    Button processOrderButton;

    String customerDevice;
    Customer customer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentFragment = inflater.inflate(R.layout.fragment_customer_new, container, false);

        app = (MobileApp) getActivity().getApplicationContext();
        //getActivity().setTitle(getString(R.string.title_estimate));

        setupUi();
        updateValues();

        return contentFragment;
    }

    private void setupUi() {
        processOrderButton = (Button) contentFragment.findViewById(R.id.submitItem);

        customerName = (TextView) contentFragment.findViewById(R.id.customerName);
        customerDeviceName = (TextView) contentFragment.findViewById(R.id.customerDeviceName);
        customerImage = (ImageView) contentFragment.findViewById(R.id.itemImage);
        customerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(cameraIntent, MainActivity.CAMERA_REQUEST);
            }
        });

        processOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getCustomers().add(customer);

                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.customer_process_order))
                        .setMessage("New customer added")
                        .show();


            }
        });
    }

    private void updateValues() {
        customer = app.getUnknownDevice(customerDevice);
        customer.setName("John Doe");
        customer.setPicture(getResources().getDrawable(R.drawable.profile_none));

        customerName.setText(customer.getName());
        customerDeviceName.setText(customer.getDevice());
        customerImage.setImageDrawable(customer.getPicture());
    }

    public void updatePhoto(Drawable photo) {
        customer.setPicture(photo);
        customerImage.setImageDrawable(photo);
        customerImage.invalidate();
    }

    @Override
    public void onStart() {
        super.onStart();
        //showSampleItem();
    }

    public String getCustomerDevice() {
        return customerDevice;
    }

    public void setCustomerDevice(String customerDevice) {
        this.customerDevice = customerDevice;
    }
}
