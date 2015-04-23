package com.steppschuh.remerchant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerListAdapter extends ArrayAdapter<Customer> {

    ArrayList<Customer> items = new ArrayList<>();
    Activity context;

    public CustomerListAdapter(Activity context, int resource, ArrayList<Customer> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public Customer getItem(int position) {
        return items.get(position);
    }

    public void setItems(ArrayList<Customer> items) {
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        Customer currentItem = getItem(position);

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.customer_list_item, null);
            rowView.setTag(currentItem.getDevice());
        }

        // fill data

        ((TextView) rowView.findViewById(R.id.customerName)).setText(currentItem.getName());
        ((TextView) rowView.findViewById(R.id.customerLastSeenValue)).setText(currentItem.getLastSeenString());

        if (currentItem.getLoyality() > 0) {
            ((TextView) rowView.findViewById(R.id.customerLoyality)).setText(String.valueOf(currentItem.getLoyality()));
            ((TextView) rowView.findViewById(R.id.customerWorth)).setText((currentItem.getLoyality() * 3)  + "€");
        } else {
            ((TextView) rowView.findViewById(R.id.customerLoyality)).setVisibility(View.GONE);
            ((TextView) rowView.findViewById(R.id.customerWorth)).setVisibility(View.GONE);
        }

        // load image
        ImageView itemImage = (ImageView) rowView.findViewById(R.id.customerPicture);
        itemImage.setImageDrawable(currentItem.getPicture());

        return rowView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

}
