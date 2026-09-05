package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.adapters.RecipeAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private ListView listRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipes);

        listRecipes = findViewById(R.id.listRecipes);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.seedDefaultRecipes();

        loadRecipes();
    }

    private void loadRecipes() {

        ArrayList<String> recipes = new ArrayList<>();
        ArrayList<Integer> recipeIds = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT id, name FROM recipes",
                null
        );

        while (cursor.moveToNext()) {

            int recipeId = cursor.getInt(0);
            String recipeName = cursor.getString(1);

            recipeIds.add(recipeId);
            recipes.add(recipeName);
        }

        cursor.close();

        RecipeAdapter adapter = new RecipeAdapter(
                this,
                recipes
        );

        listRecipes.setAdapter(adapter);

        listRecipes.setOnItemClickListener((parent, view, position, id) -> {

            int selectedRecipeId = recipeIds.get(position);

            Intent intent = new Intent(
                    RecipesActivity.this,
                    RecipeDetailActivity.class
            );

            intent.putExtra("recipe_id", selectedRecipeId);

            startActivity(intent);
        });
    }
}