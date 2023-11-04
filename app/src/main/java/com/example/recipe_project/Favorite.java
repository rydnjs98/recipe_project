package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        if(user != null){
            user_details.setText(user.getEmail());
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(getApplicationContext(), Favorite.class);
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
