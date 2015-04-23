package com.steppschuh.remerchant;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentCustomers extends Fragment implements CustomersChangedListener {

    MobileApp app;
    Activity contextActivity;

    public static final int TYPE_ALL_CUSTOMERS = 0;
    public static final int TYPE_RECENT_CUSTOMERS = 1;
    public static final int TYPE_DEVICES = 2;

    View contentFragment;
    ListView customerListView;
    CustomerListAdapter customerListAdapter;
    ArrayList<Customer> customers = new ArrayList<>();
    int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentFragment = inflater.inflate(R.layout.fragment_customers, container, false);

        app = (MobileApp) getActivity().getApplicationContext();
        app.addCustomersChangedListener(this);
        //getActivity().setTitle(getString(R.string.title_ranking));

        setupUi();

        Log.d(MobileApp.TAG, "Creating customer list fragment");

        return contentFragment;
    }

    private void setupUi() {
        customerListView = (ListView) contentFragment.findViewById(R.id.estimatedItemsList);
        customerListView.setClickable(true);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = (String) view.getTag();
                Log.d(MobileApp.TAG, "Clicked: "+ deviceName);
                app.showCustomerDetails(deviceName);
            }
        });

        customers = getCustomersToDisplay();
        customerListAdapter = new CustomerListAdapter(getActivity(), R.layout.customer_list_item, customers);

        customerListView.setAdapter(customerListAdapter);
    }

    private ArrayList<Customer> getCustomersToDisplay() {
        ArrayList<Customer> customersToDisplay = new ArrayList<>();
        switch (type) {
            case TYPE_ALL_CUSTOMERS: {
                customersToDisplay = app.getCustomers();
                //Collections.sort(customersToDisplay, new Customer.LastSeenComparator());
                break;
            }
            case TYPE_RECENT_CUSTOMERS: {
                for (Customer customer : app.getCustomers()) {
                    if (customer.recentlySeen()) {
                        customersToDisplay.add(customer);
                    }
                }
                //Collections.sort(customersToDisplay, new Customer.LastSeenComparator());
                break;
            }
            case TYPE_DEVICES: {
                for (Customer customer : app.getUnknownCustomers()) {
                    if (customer.recentlySeen()) {
                        customersToDisplay.add(customer);
                    }
                }
                //Collections.sort(customersToDisplay, new Customer.LastSeenComparator());
                break;
            }
        }

        return customersToDisplay;
    }

    @Override
    public void onCustomersChanged() {
        customers = getCustomersToDisplay();
        customerListAdapter.setItems(customers);

        synchronized (customerListAdapter) {
            contextActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customerListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        contextActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
