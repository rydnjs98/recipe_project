package com.example.recipe_project;

import static com.example.recipe_project.DataAdapter.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String TAG = "start";

        db.collection("recipe")
                .whereEqualTo("recipe_ID", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Recipe_Post> list2 = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Recipe_Post recipe2 = document.toObject(Recipe_Post.class);
                                Log.d(TAG, "value : " + recipe2.Recipe_getname());
                                list2.add(recipe2);
                            }
                            for (Recipe_Post recipe2 : list2){
                                Log.d(TAG, "recipe name = " + recipe2.Recipe_getname());
                                Log.d(TAG, "recipe tag = " + recipe2.Recipe_gettag());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
