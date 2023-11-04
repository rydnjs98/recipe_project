package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class Search extends AppCompatActivity {

    Button tomain;
    Button tofavorite;

    EditText getedt;
    String gotedt;

    TextView tag1, tag2, tag3, tag4;
    private boolean isCode = false;
    private boolean isEmail = false;
    private boolean isUsername = false;
    private HashMap<String, Object> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);




       RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /* initiate adapter */
        MyRecyclerAdapter mRecyclerAdapter = new MyRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<FindItem> mfindItems = new ArrayList<>();
        DatabaseReference fbdb = FirebaseDatabase.getInstance().getReference("recipe");
        for(int i=1;i<=10;i++){
            if(i%2==0)
                mfindItems.add(new FindItem(R.drawable.ic_launcher_foreground,i+"번째 사람",i+"번째 상태메시지"));
            else
                mfindItems.add(new FindItem(R.drawable.ic_launcher_foreground,i+"번째 사람",i+"번째 상태메시지"));

        }
        mRecyclerAdapter.setFindList(mfindItems);


        tomain = findViewById(R.id.search_main_btn);
        tofavorite = findViewById(R.id.search_favotie_btn);
        getedt = findViewById(R.id.search_edt);
        tag1 = findViewById(R.id.search_textView);
        tag2 = findViewById(R.id.search_textView2);
        tag3 = findViewById(R.id.search_textView3);
        tag4 = findViewById(R.id.search_textView4);
        tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tofavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, Favorite.class);
                startActivity(intent);
            }
        });
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag1.getText().toString());
            }
        });
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag2.getText().toString());
            }
        });
        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag3.getText().toString());
            }
        });
        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag4.getText().toString());
            }
        });

        gotedt = getedt.getText().toString();


    }





}
