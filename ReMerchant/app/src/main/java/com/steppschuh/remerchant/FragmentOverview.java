package com.steppschuh.remerchant;

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

public class FragmentOverview extends Fragment {

    MobileApp app;

    View contentFragment;
    SeekBar seekBar;
    TextView loyalityValue;
    TextView customerName;
    RelativeLayout customerNameContainer;
    ImageView customerImage;
    Button processOrderButton;
    Button offerPromoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentFragment = inflater.inflate(R.layout.fragment_overview, container, false);

        app = (MobileApp) getActivity().getApplicationContext();
        //getActivity().setTitle(getString(R.string.title_estimate));

        setupUi();

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
                //updatePriceValue(seekValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //int price = currentItem.percentageToPrice(seekValue);
                //currentItem.setEstimatedPrice(price);
            }
        });

        offerPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        processOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //showSampleItem();
    }
}
