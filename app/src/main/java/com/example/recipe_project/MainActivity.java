package com.example.recipe_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button tosearch;
    Button tofavorite;
    ImageButton recepices_btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       tosearch = findViewById(R.id.main_search_btn);
       tofavorite = findViewById(R.id.main_favorite_btn);
        recepices_btn1=findViewById(R.id.recepices_btn1);

        ViewPager2 viewPager_idol = findViewById(R.id.viewPager_idol);
        viewPager_idol.setAdapter(new ViewPagerAdapter(getIdolList()));
        viewPager_idol.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);


        recepices_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Recipe.class);
                startActivity(intent);
            }
        });

        tosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, Search.class);
               startActivity(intent);
            }
        });
        tofavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Favorite.class);
                startActivity(intent);
            }
        });

    }
    private ArrayList<Integer> getIdolList() {
        ArrayList<Integer> itemList = new ArrayList<>();
        itemList.add(R.drawable.idol1);
        itemList.add(R.drawable.idol2);
        itemList.add(R.drawable.idol3);
        return itemList;
    }


}