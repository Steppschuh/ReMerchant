package com.steppschuh.remerchant;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.aevi.payment.PaymentRequest;

import java.math.BigDecimal;
import java.util.Currency;

public class FragmentCustomerDetail extends Fragment implements CustomersChangedListener {

    MobileApp app;

    View contentFragment;
    SeekBar seekBar;
    TextView loyalityValue;
    TextView customerName;
    RelativeLayout customerNameContainer;
    ImageView customerImage;
    Button processOrderButton;
    Button offerPromoButton;

    String customerDevice;
    Customer customer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentFragment = inflater.inflate(R.layout.fragment_customer_detail, container, false);

        app = (MobileApp) getActivity().getApplicationContext();
        app.addCustomersChangedListener(this);
        //getActivity().setTitle(getString(R.string.title_estimate));

        setupUi();
        updateValues();

        return contentFragment;
    }

    private void setupUi() {
        offerPromoButton = (Button) contentFragment.findViewById(R.id.skipItem);
        processOrderButton = (Button) contentFragment.findViewById(R.id.submitItem);

        customerName = (TextView) contentFragment.findViewById(R.id.itemNameLabel);
        customerNameContainer = (RelativeLayout) contentFragment.findViewById(R.id.itemNameContainer);
        loyalityValue = (TextView) contentFragment.findViewById(R.id.estimatePriceValue);
        customerImage = (ImageView) contentFragment.findViewById(R.id.itemImage);

        seekBar = (SeekBar) contentFragment.findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue = progress;
                loyalityValue.setText(seekValue + " / 10");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //int price = currentItem.percentageToPrice(seekValue);
                //currentItem.setEstimatedPrice(price);
                customer.setLoyality(seekValue);
                if (customer.getLoyality() == 10) {
                    offerPromoButton.setEnabled(true);
                } else {
                    offerPromoButton.setEnabled(false);
                }
            }
        });

        offerPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PaymentRequest payment = new PaymentRequest(new BigDecimal("1.00"));
                    payment.setCurrency(Currency.getInstance("EUR"));
                    getActivity().startActivityForResult(payment.createIntent(), MainActivity.PAYMENT_REQUEST);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.customer_process_order))
                            .setMessage("Invoking Albert payment flow with promotional discount")
                            .show();
                }
            }
        });

        processOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer.getLoyality() < 10) {
                    customer.setLoyality(customer.getLoyality() + 1);
                    loyalityValue.setText(customer.getLoyality() + " / 10");
                    seekBar.setProgress(customer.getLoyality());
                }

                try {
                    PaymentRequest payment = new PaymentRequest(new BigDecimal("3.00"));
                    payment.setCurrency(Currency.getInstance("EUR"));
                    getActivity().startActivityForResult(payment.createIntent(), MainActivity.PAYMENT_REQUEST);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.customer_process_order))
                            .setMessage("Invoking Albert payment flow")
                            .show();
                }
            }
        });
    }

    private void updateValues() {
        customer = app.getCustomerByDevice(customerDevice);

        customerName.setText(customer.getName());
        customerImage.setImageDrawable(customer.getPicture());

        seekBar.setProgress(customer.getLoyality());
        loyalityValue.setText(customer.getLoyality() + " / 10");

        if (customer.getLoyality() == 10) {
            offerPromoButton.setEnabled(true);
        } else {
            offerPromoButton.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //showSampleItem();
    }

    @Override
    public void onCustomersChanged() {
        synchronized (this) {
            this.setupUi();
        }
    }

    public String getCustomerDevice() {
        return customerDevice;
    }

    public void setCustomerDevice(String customerDevice) {
        this.customerDevice = customerDevice;
    }
}
