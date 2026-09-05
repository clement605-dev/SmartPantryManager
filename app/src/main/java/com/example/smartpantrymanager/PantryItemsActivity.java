package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.adapters.PantryAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;

import java.util.ArrayList;

public class PantryItemsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listPantryItems;
    private TextView tvEmptyPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry_items);

        dbHelper = new DatabaseHelper(this);

        listPantryItems = findViewById(R.id.listPantryItems);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);

        loadPantryItems();
    }

    private void loadPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, quantity, unit, expiry_date FROM pantry_items",
                null
        );

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            double quantity = cursor.getDouble(2);
            String unit = cursor.getString(3);
            String expiryDate = cursor.getString(4);

            pantryItems.add(
                    new PantryItem(
                            id,
                            name,
                            quantity,
                            unit,
                            expiryDate
                    )
            );
        }

        cursor.close();
        db.close();

        PantryAdapter adapter = new PantryAdapter(
                this,
                pantryItems,

                item -> {
                    int deleted = dbHelper.deletePantryItem(item.getId());

                    if (deleted > 0) {
                        Toast.makeText(
                                PantryItemsActivity.this,
                                "Pantry item deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadPantryItems();
                    }
                },

                item -> {
                    Intent intent = new Intent(
                            PantryItemsActivity.this,
                            AddPantryItemActivity.class
                    );

                    intent.putExtra("item_id", item.getId());
                    intent.putExtra("item_name", item.getName());
                    intent.putExtra("item_quantity", item.getQuantity());
                    intent.putExtra("item_unit", item.getUnit());
                    intent.putExtra("item_expiry", item.getExpiryDate());

                    startActivity(intent);
                }
        );

        listPantryItems.setAdapter(adapter);

        // Show the empty message when there are no pantry items
        if (pantryItems.isEmpty()) {
            listPantryItems.setVisibility(View.GONE);
            tvEmptyPantry.setVisibility(View.VISIBLE);
        } else {
            listPantryItems.setVisibility(View.VISIBLE);
            tvEmptyPantry.setVisibility(View.GONE);
        }
    }
}