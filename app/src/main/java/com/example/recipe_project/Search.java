package com.example.recipe_project;

import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {

    Button tomain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        tomain = findViewById(R.id.search_main_btn);
    }
}
