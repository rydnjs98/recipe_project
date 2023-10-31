package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Favorite extends AppCompatActivity {

    Button tosearch;
    Button tomain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        tosearch = findViewById(R.id.favorite_search_btn);
        tomain = findViewById(R.id.favorite_main_btn);


        tosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorite.this, Search.class);
                startActivity(intent);
            }
        });
        tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorite.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
