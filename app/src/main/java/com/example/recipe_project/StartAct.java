package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StartAct  extends AppCompatActivity {

    ImageView img1, img2, img3, img4;

    TextView txt1, txt2, txt3, txt4;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        img1 = findViewById(R.id.start_img1);
        img2 = findViewById(R.id.start_img2);
        img3 = findViewById(R.id.start_img3);
        img4 = findViewById(R.id.start_img4);

        txt1 = findViewById(R.id.start_txt1);
        txt2 = findViewById(R.id.start_txt2);
        txt3 = findViewById(R.id.start_txt3);
        txt4 = findViewById(R.id.start_txt4);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        postFirebaseDatabase(true);
    }
    int ID = 21;
    String name = "밥";
    String link = "";
    String IDs = "";
    String tag = "";
    String info = "";
    DatabaseReference mPostReference = null;

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance("https://recipe-2023-team2-default-rtdb.firebaseio.com").getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            Recipe_Post post = new Recipe_Post(ID, name, link, IDs, tag, info);
            postValues = post.toMap();
        }
        childUpdates.put("/recipe/" + ID, postValues);
        mPostReference.updateChildren(childUpdates);
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                Log.d("Database", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Database", "Failed to read value.", error.toException());
            }
        });
    }
}
