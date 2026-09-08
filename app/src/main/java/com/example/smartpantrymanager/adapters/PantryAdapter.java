package com.example.smartpantrymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.PantryItem;

import java.util.List;

public class PantryAdapter extends BaseAdapter {

    private Context context;
    private List<PantryItem> pantryItems;
    private OnDeleteClickListener deleteListener;
    private OnEditClickListener editListener;

    public PantryAdapter(Context context, List<PantryItem> pantryItems,
                         OnDeleteClickListener deleteListener,
                         OnEditClickListener editListener) {
        this.context = context;
        this.pantryItems = pantryItems;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }
    @Override
    public int getCount() {
        return pantryItems.size();
    }

    @Override
    public Object getItem(int position) {
        return pantryItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pantryItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_pantry, parent, false);
        }

        TextView tvItemName = convertView.findViewById(R.id.tvItemName);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
        TextView tvExpiry = convertView.findViewById(R.id.tvExpiry);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);

        PantryItem item = pantryItems.get(position);

        tvItemName.setText(item.getName());
        tvQuantity.setText(
                "Quantity: " + item.getQuantity() + " " + item.getUnit()
        );
        String expiryDate = item.getExpiryDate();

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            tvExpiry.setText("Expiry: Not specified");
        } else {
            tvExpiry.setText("Expiry: " + expiryDate);
        }
        btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(item);
            }
        });
        btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEdit(item);
            }
        });

        return convertView;
    }
    public interface OnDeleteClickListener {
        void onDelete(PantryItem item);
    }

    public interface OnEditClickListener {
        void onEdit(PantryItem item);
    }
}