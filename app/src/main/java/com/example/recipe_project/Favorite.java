package com.example.recipe_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Favorite extends AppCompatActivity {

    Button tosearch, tomain, tofavorite;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView user_details;
    Button login, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        tosearch = findViewById(R.id.favorite_search_btn);
        tomain = findViewById(R.id.favorite_main_btn);
        tofavorite = findViewById(R.id.favorite_favorite_btn);

        login=findViewById(R.id.Favorite_login_btn);
        logout = findViewById(R.id.Favorite_logout_btn);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_details = findViewById(R.id.user_details);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(user != null){
            user_details.setText(user.getEmail());
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);

            String TAG = "Favorite";
            String email = user.getEmail();

            db.collection("favorite")
                    .whereEqualTo("user_id", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                List<String> dataArray = new ArrayList<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + "=>" + document.getData());
                                    List<String> arrayData = (List<String>) document.get("recipe_ID");

                                    if (arrayData != null) {
                                        dataArray.addAll(arrayData);
                                    }

                                    for(int i = 0 ; i < arrayData.size() ; i++) {
                                        db.collection("recipe")
                                                .whereEqualTo("recipe_ID", arrayData.get(i))
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Log.d(TAG, document.getId() + "=>" + document.getData().get("recipe_name"));

                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });
                                    }

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        } else {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_details.setText(null);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



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

        tofavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorite.this, Favorite.class);
                startActivity(intent);
            }
        });

    }
}
