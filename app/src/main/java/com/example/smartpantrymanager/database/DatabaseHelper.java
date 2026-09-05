package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Database tables
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Pantry items table
        db.execSQL(
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "expiry_date TEXT" +
                        ")"
        );

        // Recipes table
        db.execSQL(
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "instructions TEXT NOT NULL" +
                        ")"
        );

        // Recipe ingredients table
        db.execSQL(
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "required_quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES " +
                        TABLE_RECIPES + "(id) ON DELETE CASCADE" +
                        ")"
        );

        // Add the default recipes when the database is first created
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_RECIPE_INGREDIENTS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_RECIPES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_PANTRY
        );

        onCreate(db);
    }

    // =========================================================
    // PANTRY CRUD
    // =========================================================

    public long addPantryItem(
            String name,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        return db.insert(
                TABLE_PANTRY,
                null,
                values
        );
    }

    public int updatePantryItem(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        return db.update(
                TABLE_PANTRY,
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // =========================================================
    // RECIPE METHODS
    // =========================================================

    public long addRecipe(
            String name,
            String instructions) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("instructions", instructions);

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    public long addRecipeIngredient(
            long recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName);
        values.put("required_quantity", requiredQuantity);
        values.put("unit", unit);

        return db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }

    // =========================================================
    // DEFAULT RECIPE SEEDING
    // =========================================================

    public void seedDefaultRecipes() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_RECIPES,
                null
        );

        int recipeCount = 0;

        if (cursor.moveToFirst()) {
            recipeCount = cursor.getInt(0);
        }

        cursor.close();

        // Do not add recipes again if they already exist
        if (recipeCount > 0) {
            return;
        }

        seedRecipes(db);
    }

    private void seedRecipes(SQLiteDatabase db) {

        // Recipe 1
        addRecipeWithIngredients(
                db,
                "Tomato Pasta",
                "1. Boil the pasta until tender. " +
                        "2. Cook the tomatoes with onion and garlic. " +
                        "3. Add the cooked pasta and mix well. " +
                        "4. Serve hot.",
                new String[][]{
                        {"pasta", "200", "g"},
                        {"tomato", "2", "pieces"},
                        {"onion", "1", "piece"},
                        {"garlic", "2", "cloves"}
                }
        );

        // Recipe 2
        addRecipeWithIngredients(
                db,
                "Pancakes",
                "1. Mix flour, eggs, milk and sugar. " +
                        "2. Heat a pan. " +
                        "3. Pour the mixture into the pan. " +
                        "4. Cook both sides until golden.",
                new String[][]{
                        {"flour", "200", "g"},
                        {"egg", "2", "pieces"},
                        {"milk", "250", "ml"},
                        {"sugar", "30", "g"}
                }
        );

        // Recipe 3
        addRecipeWithIngredients(
                db,
                "Omelette",
                "1. Beat the eggs. " +
                        "2. Add onion and tomato. " +
                        "3. Heat a pan and add the mixture. " +
                        "4. Cook until the egg is set.",
                new String[][]{
                        {"egg", "3", "pieces"},
                        {"onion", "1", "piece"},
                        {"tomato", "1", "piece"}
                }
        );

        // Recipe 4
        addRecipeWithIngredients(
                db,
                "Chicken Sandwich",
                "1. Cook the chicken. " +
                        "2. Place chicken on bread. " +
                        "3. Add tomato and lettuce. " +
                        "4. Serve.",
                new String[][]{
                        {"chicken", "150", "g"},
                        {"bread", "2", "slices"},
                        {"tomato", "1", "piece"},
                        {"lettuce", "2", "leaves"}
                }
        );

        // Recipe 5
        addRecipeWithIngredients(
                db,
                "Fried Rice",
                "1. Cook the rice. " +
                        "2. Fry the onion and vegetables. " +
                        "3. Add the rice and mix. " +
                        "4. Add egg and cook thoroughly.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"egg", "2", "pieces"},
                        {"onion", "1", "piece"},
                        {"mixed vegetables", "100", "g"}
                }
        );

        // Recipe 6
        addRecipeWithIngredients(
                db,
                "Scrambled Eggs on Toast",
                "1. Beat the eggs. " +
                        "2. Scramble them in a heated pan. " +
                        "3. Toast the bread. " +
                        "4. Serve the eggs on the toast.",
                new String[][]{
                        {"egg", "2", "pieces"},
                        {"bread", "2", "slices"}
                }
        );

        // Recipe 7
        addRecipeWithIngredients(
                db,
                "Chicken Curry",
                "1. Cook the onion and garlic. " +
                        "2. Add chicken and curry spices. " +
                        "3. Add tomatoes and simmer. " +
                        "4. Cook until the chicken is done.",
                new String[][]{
                        {"chicken", "300", "g"},
                        {"onion", "1", "piece"},
                        {"tomato", "2", "pieces"},
                        {"garlic", "2", "cloves"}
                }
        );

        // Recipe 8
        addRecipeWithIngredients(
                db,
                "Vegetable Stir Fry",
                "1. Cut the vegetables. " +
                        "2. Heat a pan. " +
                        "3. Stir-fry the vegetables until tender. " +
                        "4. Serve hot.",
                new String[][]{
                        {"carrot", "2", "pieces"},
                        {"onion", "1", "piece"},
                        {"pepper", "1", "piece"}
                }
        );

        // Recipe 9
        addRecipeWithIngredients(
                db,
                "Tuna Sandwich",
                "1. Mix tuna with mayonnaise. " +
                        "2. Place the mixture on bread. " +
                        "3. Add lettuce. " +
                        "4. Serve.",
                new String[][]{
                        {"tuna", "1", "can"},
                        {"bread", "2", "slices"},
                        {"lettuce", "2", "leaves"}
                }
        );

        // Recipe 10
        addRecipeWithIngredients(
                db,
                "Rice and Beans",
                "1. Cook the rice. " +
                        "2. Heat the beans with onion and tomato. " +
                        "3. Combine and serve.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"beans", "200", "g"},
                        {"onion", "1", "piece"},
                        {"tomato", "1", "piece"}
                }
        );

        // Recipe 11
        addRecipeWithIngredients(
                db,
                "Chicken Rice",
                "1. Cook the chicken. " +
                        "2. Cook the rice. " +
                        "3. Fry onion and add the chicken. " +
                        "4. Mix with rice and serve.",
                new String[][]{
                        {"chicken", "200", "g"},
                        {"rice", "200", "g"},
                        {"onion", "1", "piece"}
                }
        );

        // Recipe 12
        addRecipeWithIngredients(
                db,
                "French Toast",
                "1. Beat eggs with milk. " +
                        "2. Dip bread into the mixture. " +
                        "3. Fry both sides until golden. " +
                        "4. Serve.",
                new String[][]{
                        {"bread", "2", "slices"},
                        {"egg", "2", "pieces"},
                        {"milk", "100", "ml"}
                }
        );

        // Recipe 13
        addRecipeWithIngredients(
                db,
                "Beef Stew",
                "1. Brown the beef. " +
                        "2. Add onion and vegetables. " +
                        "3. Add water and simmer. " +
                        "4. Cook until the beef is tender.",
                new String[][]{
                        {"beef", "300", "g"},
                        {"onion", "1", "piece"},
                        {"carrot", "2", "pieces"},
                        {"potato", "2", "pieces"}
                }
        );

        // Recipe 14
        addRecipeWithIngredients(
                db,
                "Mashed Potatoes",
                "1. Boil the potatoes until soft. " +
                        "2. Drain the water. " +
                        "3. Mash the potatoes. " +
                        "4. Add milk and mix.",
                new String[][]{
                        {"potato", "4", "pieces"},
                        {"milk", "100", "ml"}
                }
        );

        // Recipe 15
        addRecipeWithIngredients(
                db,
                "Egg Fried Rice",
                "1. Cook the rice. " +
                        "2. Scramble the eggs. " +
                        "3. Add onion and rice. " +
                        "4. Stir-fry everything together.",
                new String[][]{
                        {"rice", "200", "g"},
                        {"egg", "2", "pieces"},
                        {"onion", "1", "piece"}
                }
        );
    }

    private void addRecipeWithIngredients(
            SQLiteDatabase db,
            String name,
            String instructions,
            String[][] ingredients) {

        ContentValues recipeValues = new ContentValues();

        recipeValues.put("name", name);
        recipeValues.put("instructions", instructions);

        long recipeId = db.insert(
                TABLE_RECIPES,
                null,
                recipeValues
        );

        if (recipeId == -1) {
            return;
        }

        for (String[] ingredient : ingredients) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    "recipe_id",
                    recipeId
            );

            ingredientValues.put(
                    "ingredient_name",
                    ingredient[0]
            );

            ingredientValues.put(
                    "required_quantity",
                    Double.parseDouble(ingredient[1])
            );

            ingredientValues.put(
                    "unit",
                    ingredient[2]
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }
}