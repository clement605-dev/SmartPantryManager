# Smart Pantry Manager

Smart Pantry Manager is a Java-based Android application designed to help users manage pantry ingredients and find recipes that can be prepared using the ingredients currently available.

## Features

- Add pantry ingredients
- Edit existing pantry ingredients
- Delete pantry ingredients
- Store ingredient quantity and unit
- Store an optional expiry date
- View all pantry items
- View a list of available recipes
- View recipe ingredients and preparation instructions
- Suggest recipes based on the ingredients in the pantry
- Strict recipe matching based on required ingredients and quantities
- Empty-state messages when there are no pantry items or matching recipes
- Settings screen with application information
- Data remains stored after closing and reopening the application

## Technologies Used

- Java
- Android Studio
- Android SDK
- SQLite
- Git
- GitHub

## Database

The application uses SQLite for local data storage.

SQLite was chosen because the application is designed to store pantry and recipe information directly on the user's device. It does not require an internet connection or a separate database server, making it suitable for this small mobile application.

The database contains three main tables:

- `pantry_items` - stores pantry ingredients
- `recipes` - stores recipe names and preparation instructions
- `recipe_ingredients` - stores the ingredients and quantities required for each recipe

## Recipe Matching

The application only suggests a recipe when all required ingredients are available in the pantry in sufficient quantities.

For example, if a recipe requires:

- 200 g rice
- 2 eggs
- 1 onion

the recipe will only be suggested when the pantry contains at least those required amounts.

Partial matches are not suggested.

The application also supports simple unit conversions such as:

- kilograms to grams
- litres to millilitres

Simple singular and plural ingredient differences are also handled.

## Application Screens

The application contains the following main screens:

1. Pantry Items
2. Add/Edit Pantry Item
3. Suggested Recipes
4. Recipe Detail
5. Settings

## How to Run

1. Clone or download the project from GitHub.
2. Open the project in Android Studio.
3. Allow Android Studio to complete Gradle synchronization.
4. Connect an Android device or start an Android Emulator.
5. Run the application using the Run button in Android Studio.

## Project Structure

```text
SmartPantryManager
│
├── app
│   └── src
│       └── main
│           ├── java
│           │   └── com.example.smartpantrymanager
│           │       ├── AddPantryItemActivity.java
│           │       ├── MainActivity.java
│           │       ├── PantryItemsActivity.java
│           │       ├── RecipesActivity.java
│           │       ├── SuggestedRecipesActivity.java
│           │       ├── RecipeDetailActivity.java
│           │       ├── SettingsActivity.java
│           │       ├── PantryItem.java
│           │       ├── adapters
│           │       ├── database
│           │       └── models
│           │
│           └── res
│               ├── layout
│               ├── drawable
│               └── values
│
├── gradle
├── README.md
├── build.gradle.kts
└── settings.gradle.kts