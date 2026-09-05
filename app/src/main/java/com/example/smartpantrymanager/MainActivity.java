package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);

        Button pantryItems = findViewById(R.id.btnPantry);

        pantryItems.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    PantryItemsActivity.class
            );

            startActivity(intent);
        });

        Button addPantryItem = findViewById(R.id.btnAddPantryItem);

        addPantryItem.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddPantryItemActivity.class
            );

            startActivity(intent);
        });

        Button recipes = findViewById(R.id.btnRecipes);

        recipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RecipesActivity.class
            );

            startActivity(intent);
        });

        Button viewRecipes = findViewById(R.id.btnViewRecipes);

        viewRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        Button settings = findViewById(R.id.btnSettings);

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }
}