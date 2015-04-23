package com.steppschuh.remerchant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Collections;

public class FragmentCustomersPager extends Fragment{

    MobileApp app;
    View contentFragment;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentFragment = inflater.inflate(R.layout.fragment_customers_pager, container, false);

        app = (MobileApp) getActivity().getApplicationContext();
        //getActivity().setTitle(getString(R.string.title_ranking));

        setupUi();

        return contentFragment;
    }

    private void setupUi() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) contentFragment.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case FragmentCustomers.TYPE_ALL_CUSTOMERS: {
                    FragmentCustomers fragment = new FragmentCustomers();
                    fragment.setType(position);
                    return fragment;
                }
                case FragmentCustomers.TYPE_RECENT_CUSTOMERS: {
                    FragmentCustomers fragment = new FragmentCustomers();
                    fragment.setType(position);
                    return fragment;
                }
                case FragmentCustomers.TYPE_DEVICES: {
                    FragmentCustomers fragment = new FragmentCustomers();
                    fragment.setType(position);
                    return fragment;
                }
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case FragmentCustomers.TYPE_ALL_CUSTOMERS: {
                    return getString(R.string.title_all_customers);
                }
                case FragmentCustomers.TYPE_RECENT_CUSTOMERS: {
                    return getString(R.string.title_recent_customers);
                }
                case FragmentCustomers.TYPE_DEVICES: {
                    return getString(R.string.title_all_devices);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
