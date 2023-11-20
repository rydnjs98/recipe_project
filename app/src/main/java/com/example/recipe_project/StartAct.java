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

    ImageView imgWes, imgJap, imgChi, imgKor;

    TextView txtWes, txtJap, txtChi, txtKor;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imgWes = findViewById(R.id.start_imgWes);
        imgJap = findViewById(R.id.start_imgJap);
        imgChi = findViewById(R.id.start_imgChi);
        imgKor = findViewById(R.id.start_imgKor);

        txtWes = findViewById(R.id.start_txtWes);
        txtJap = findViewById(R.id.start_txtJap);
        txtChi = findViewById(R.id.start_txtChi);
        txtKor = findViewById(R.id.start_txtKor);

        imgWes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "양식");
                startActivity(intent);
            }
        });
        imgJap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "일식");
                startActivity(intent);
            }
        });
        imgChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "중식");
                startActivity(intent);
            }
        });
        imgKor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "한식");
                startActivity(intent);
            }
        });

        txtWes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "양식");
                startActivity(intent);
            }
        });
        txtJap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "일식");
                startActivity(intent);
            }
        });
        txtChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "중식");
                startActivity(intent);
            }
        });
        txtKor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAct.this, MainActivity.class);
                intent.putExtra("태그", "한식");
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
