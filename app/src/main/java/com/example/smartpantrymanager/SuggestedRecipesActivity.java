package com.example.smartpantrymanager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private ListView listSuggestedRecipes;
    private TextView tvEmptySuggestions;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        listSuggestedRecipes = findViewById(R.id.listSuggestedRecipes);
        tvEmptySuggestions = findViewById(R.id.tvEmptySuggestions);

        dbHelper = new DatabaseHelper(this);

        // Make sure the default recipes exist
        dbHelper.seedDefaultRecipes();

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {

        ArrayList<String> suggestedRecipes = new ArrayList<>();
        ArrayList<Integer> suggestedRecipeIds = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor recipeCursor = db.rawQuery(
                "SELECT id, name FROM recipes",
                null
        );

        while (recipeCursor.moveToNext()) {

            int recipeId = recipeCursor.getInt(0);
            String recipeName = recipeCursor.getString(1);

            if (canMakeRecipe(db, recipeId)) {
                suggestedRecipes.add(recipeName);
                suggestedRecipeIds.add(recipeId);
            }
        }

        recipeCursor.close();

        if (suggestedRecipes.isEmpty()) {

            listSuggestedRecipes.setVisibility(ListView.GONE);
            tvEmptySuggestions.setVisibility(TextView.VISIBLE);

        } else {

            listSuggestedRecipes.setVisibility(ListView.VISIBLE);
            tvEmptySuggestions.setVisibility(TextView.GONE);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    suggestedRecipes
            );

            listSuggestedRecipes.setAdapter(adapter);
            listSuggestedRecipes.setOnItemClickListener((parent, view, position, id) -> {

                int selectedRecipeId = suggestedRecipeIds.get(position);

                Intent intent = new Intent(
                        SuggestedRecipesActivity.this,
                        RecipeDetailActivity.class
                );

                intent.putExtra("recipe_id", selectedRecipeId);

                startActivity(intent);
            });
        }
    }

    private boolean canMakeRecipe(SQLiteDatabase db, int recipeId) {

        Cursor ingredientCursor = db.rawQuery(
                "SELECT ingredient_name, required_quantity, unit " +
                        "FROM recipe_ingredients " +
                        "WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        while (ingredientCursor.moveToNext()) {

            String requiredName = ingredientCursor.getString(0);
            double requiredQuantity = ingredientCursor.getDouble(1);
            String requiredUnit = ingredientCursor.getString(2);

            boolean ingredientAvailable = false;

            Cursor pantryCursor = db.rawQuery(
                    "SELECT name, quantity, unit FROM pantry_items",
                    null
            );

            while (pantryCursor.moveToNext()) {

                String pantryName = pantryCursor.getString(0);
                double pantryQuantity = pantryCursor.getDouble(1);
                String pantryUnit = pantryCursor.getString(2);

                boolean sameName =
                        normalizeIngredientName(pantryName)
                                .equals(normalizeIngredientName(requiredName));

                boolean sameUnit =
                        normalizeUnit(pantryUnit)
                                .equals(normalizeUnit(requiredUnit));

                boolean enoughQuantity =
                        pantryQuantity >= requiredQuantity;

                if (sameName && sameUnit && enoughQuantity) {
                    ingredientAvailable = true;
                    break;
                }
            }

            pantryCursor.close();

            // If even ONE ingredient is missing, the recipe cannot be made.
            if (!ingredientAvailable) {
                ingredientCursor.close();
                return false;
            }
        }

        ingredientCursor.close();

        return true;
    }

    private String normalizeIngredientName(String name) {

        String value = name.trim().toLowerCase();

        // Handle simple singular/plural differences
        if (value.endsWith("ies")) {
            value = value.substring(0, value.length() - 3) + "y";
        } else if (value.endsWith("es")) {
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("s")) {
            value = value.substring(0, value.length() - 1);
        }

        return value;
    }

    private String normalizeUnit(String unit) {

        String value = unit.trim().toLowerCase();

        if (value.equals("grams") || value.equals("gram")) {
            return "g";
        }

        if (value.equals("kilograms") || value.equals("kilogram")) {
            return "kg";
        }

        if (value.equals("millilitres") || value.equals("milliliters")
                || value.equals("millilitre") || value.equals("milliliter")) {
            return "ml";
        }

        if (value.equals("litres") || value.equals("liters")
                || value.equals("litre") || value.equals("liter")) {
            return "l";
        }

        if (value.equals("pieces") || value.equals("piece")) {
            return "piece";
        }

        if (value.equals("slices") || value.equals("slice")) {
            return "slice";
        }

        if (value.equals("cloves") || value.equals("clove")) {
            return "clove";
        }

        if (value.equals("cans") || value.equals("can")) {
            return "can";
        }

        return value;
    }
}