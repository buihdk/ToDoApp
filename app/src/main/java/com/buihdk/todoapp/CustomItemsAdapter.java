package com.buihdk.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomItemsAdapter extends ArrayAdapter<Item> {
    public CustomItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_date, parent, false);
        }

        TextView tvItemName = (TextView)convertView.findViewById(R.id.tvItemName);
        TextView tvDueDate = (TextView)convertView.findViewById(R.id.tvDueDate);

        tvItemName.setText(item.item_name);
        tvDueDate.setText(item.due_date);

        return convertView;
    }
}
