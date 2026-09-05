package com.example.smartpantrymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartpantrymanager.R;

import java.util.List;

public class RecipeAdapter extends BaseAdapter {

    private Context context;
    private List<String> recipes;

    public RecipeAdapter(Context context, List<String> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_recipe,
                    parent,
                    false
            );
        }

        TextView tvRecipeName = convertView.findViewById(R.id.tvRecipeName);

        tvRecipeName.setText(recipes.get(position));

        return convertView;
    }
}