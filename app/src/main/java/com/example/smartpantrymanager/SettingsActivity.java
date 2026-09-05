package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnAbout = findViewById(R.id.btnAbout);
        Button btnBack = findViewById(R.id.btnBack);

        btnAbout.setOnClickListener(v -> {
            Toast.makeText(
                    SettingsActivity.this,
                    "Smart Pantry Manager helps you manage pantry ingredients and find recipes you can make.",
                    Toast.LENGTH_LONG
            ).show();
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}