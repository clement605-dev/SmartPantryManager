package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;

public class AddPantryItemActivity extends AppCompatActivity {

    private EditText etItemName;
    private EditText etQuantity;
    private EditText etUnit;
    private EditText etExpiryDate;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_pantry_item);

        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        Button btnSaveItem = findViewById(R.id.btnSaveItem);

        dbHelper = new DatabaseHelper(this);

        int itemId = getIntent().getIntExtra("item_id", -1);

        if (itemId != -1) {
            etItemName.setText(getIntent().getStringExtra("item_name"));

            etQuantity.setText(
                    String.valueOf(
                            getIntent().getDoubleExtra("item_quantity", 0.0)
                    )
            );

            etUnit.setText(getIntent().getStringExtra("item_unit"));
            etExpiryDate.setText(getIntent().getStringExtra("item_expiry"));

            btnSaveItem.setText("Update Pantry Item");
        }

        btnSaveItem.setOnClickListener(v -> {

            String name = etItemName.getText().toString().trim();
            String quantity = etQuantity.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();
            String expiryDate = etExpiryDate.getText().toString().trim();

            if (name.isEmpty()
                    || quantity.isEmpty()
                    || unit.isEmpty()) {

                Toast.makeText(
                        AddPantryItemActivity.this,
                        "Please fill in the required fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            double quantityValue;

            try {
                quantityValue = Double.parseDouble(quantity);
                if (quantityValue <= 0) {
                    Toast.makeText(
                            AddPantryItemActivity.this,
                            "Quantity must be greater than 0",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }
            } catch (NumberFormatException e) {

                Toast.makeText(
                        AddPantryItemActivity.this,
                        "Please enter a valid quantity",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            boolean success;

            if (itemId == -1) {

                success = dbHelper.addPantryItem(
                        name,
                        quantityValue,
                        unit,
                        expiryDate
                ) != -1L;

            } else {

                success = dbHelper.updatePantryItem(
                        itemId,
                        name,
                        quantityValue,
                        unit,
                        expiryDate
                ) > 0;
            }

            if (success) {

                Toast.makeText(
                        AddPantryItemActivity.this,
                        itemId == -1
                                ? "Pantry item saved!"
                                : "Pantry item updated!",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        AddPantryItemActivity.this,
                        itemId == -1
                                ? "Failed to save pantry item"
                                : "Failed to update pantry item",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}