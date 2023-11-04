package com.example.recipe_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView user_details;
    Button tosearch;
    Button tofavorite;
    ImageButton login;
    Button  logout;
    ImageButton recepices_btn1;

    Button main_btn1,main_btn2,main_btn3,main_btn4;
    private boolean isFullHeart = false;
    String cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       tosearch = findViewById(R.id.main_search_btn);
       tofavorite = findViewById(R.id.main_favorite_btn);
       recepices_btn1=findViewById(R.id.recepices_btn1);

       login=findViewById(R.id.main_login_btn);
       logout = findViewById(R.id.main_logout_btn);
       auth = FirebaseAuth.getInstance();
       user = auth.getCurrentUser();
       user_details = findViewById(R.id.user_details);

        Button button = findViewById(R.id.main_btn1);
        Button button2 = findViewById(R.id.main_btn2);
        Button button3 = findViewById(R.id.main_btn3);
        Button button4 = findViewById(R.id.main_btn4);



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
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);
               finish();
           }
       });

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
                if(user != null) {
                    Intent intent = new Intent(MainActivity.this, Favorite.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "로그인을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFullHeart) {
                    button.setBackgroundResource(R.drawable.ic_emptyheart);
                } else {
                    button.setBackgroundResource(R.drawable.ic_fullheart);
                }
                isFullHeart = !isFullHeart;     //하트 변경
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