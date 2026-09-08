package com.example.smartpantrymanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeName;
    private TextView tvIngredients;
    private TextView tvInstructions;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);

        dbHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(
                    RecipeDetailActivity.this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadRecipeDetails(recipeId);
    }

    private void loadRecipeDetails(int recipeId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Get recipe name and instructions
        Cursor recipeCursor = db.rawQuery(
                "SELECT name, instructions FROM recipes WHERE id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        if (recipeCursor.moveToFirst()) {

            String recipeName = recipeCursor.getString(0);
            String instructions = recipeCursor.getString(1);

            tvRecipeName.setText(recipeName);

            tvInstructions.setText(
                    "Preparation Instructions\n\n" + instructions
            );

        } else {

            recipeCursor.close();
            db.close();

            Toast.makeText(
                    RecipeDetailActivity.this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        recipeCursor.close();

        // Get required ingredients
        Cursor ingredientCursor = db.rawQuery(
                "SELECT ingredient_name, required_quantity, unit " +
                        "FROM recipe_ingredients " +
                        "WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        StringBuilder ingredients = new StringBuilder();

        while (ingredientCursor.moveToNext()) {

            String name = ingredientCursor.getString(0);
            double quantity = ingredientCursor.getDouble(1);
            String unit = ingredientCursor.getString(2);

            ingredients.append("• ")
                    .append(name)
                    .append(" - ")
                    .append(quantity)
                    .append(" ")
                    .append(unit)
                    .append("\n");
        }

        ingredientCursor.close();
        db.close();

        tvIngredients.setText(
                "Required Ingredients\n\n" + ingredients
        );
    }
}