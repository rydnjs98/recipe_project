package com.example.recipe_project;

import static com.example.recipe_project.DataAdapter.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Search extends AppCompatActivity {

    Button tomain;
    Button tofavorite;

    EditText getedt;
    String gotedt;
    ImageButton dosearch;
    TextView tag1, tag2, tag3, tag4;

    List<FindItem> searchlist = new ArrayList<>();
    List<FindItem> gotlist = new ArrayList<>();


    private boolean isCode = false;
    private boolean isEmail = false;
    private boolean isUsername = false;
    private HashMap<String, Object> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        tomain = findViewById(R.id.search_main_btn);
        tofavorite = findViewById(R.id.search_favotie_btn);
        getedt = findViewById(R.id.search_edt);
        tag1 = findViewById(R.id.search_textView);
        tag2 = findViewById(R.id.search_textView2);
        tag3 = findViewById(R.id.search_textView3);
        tag4 = findViewById(R.id.search_textView4);
        dosearch = findViewById(R.id.search_image_btn);



        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /* initiate adapter */
        MyRecyclerAdapter mRecyclerAdapter = new MyRecyclerAdapter();

        /* initiate recyclerview */
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<FindItem> mfindItems = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docref = db.collection("recipe");


        docref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot doc : task.getResult())
                    {
                        FindItem item = doc.toObject(FindItem.class);
                        searchlist.add(item);

                    }
                    for (FindItem item : searchlist){
                        Log.d(TAG, "recipe name = " + item.getRecipe_Name());
                        Log.d(TAG, "recipe tag = " + item.getRecipe_ID());
                    }

                    mRecyclerAdapter.setFindList(searchlist);

                }else{
                    Log.d(TAG, "GET FAILED" + task.getException());
                }
            }
        });
        mRecyclerAdapter.setFindList(mfindItems);
        getedt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = getedt.getText().toString();


                gotlist.clear();
                if(text.equals("")){
                    mRecyclerAdapter.setFindList(searchlist);
                }
                else{
                    if(text.equals("일식")){
                        for(int i=0; i<searchlist.size(); i++){
                            if(searchlist.get(i).recipe_tag.contains(text)){
                                gotlist.add(searchlist.get(i));
                            }
                            mRecyclerAdapter.setFindList(gotlist);
                        }
                    }else if(text.equals("양식")){
                        for(int i=0; i<searchlist.size(); i++){
                            if(searchlist.get(i).recipe_tag.contains(text)){
                                gotlist.add(searchlist.get(i));
                            }
                            mRecyclerAdapter.setFindList(gotlist);
                        }
                    }else if(text.equals("한식")){
                        for(int i=0; i<searchlist.size(); i++){
                            if(searchlist.get(i).recipe_tag.contains(text)){
                                gotlist.add(searchlist.get(i));
                            }
                            mRecyclerAdapter.setFindList(gotlist);
                        }
                    }else if(text.equals("중식")){
                        for(int i=0; i<searchlist.size(); i++){
                            if(searchlist.get(i).recipe_tag.contains(text)){
                                gotlist.add(searchlist.get(i));
                            }
                            mRecyclerAdapter.setFindList(gotlist);
                        }
                    }else{
                        for(int i=0; i<searchlist.size(); i++){
                            if(searchlist.get(i).recipe_name.contains(text)){
                                gotlist.add(searchlist.get(i));
                            }
                            mRecyclerAdapter.setFindList(gotlist);
                        }
                    }

                }
            }
        });





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
                gotedt = getedt.getText().toString();
            }
        });
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag2.getText().toString());
                gotedt = getedt.getText().toString();
            }
        });
        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag3.getText().toString());
                gotedt = getedt.getText().toString();
            }
        });
        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getedt.setText(tag4.getText().toString());
                gotedt = getedt.getText().toString();
            }
        });





    }








}
