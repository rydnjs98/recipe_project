package com.example.recipe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    LinearLayout linearLayout;

    Button main_btn1,main_btn2,main_btn3,main_btn4;
    private boolean isFullHeart = false;
    String cu;
    String TAG = "main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       tosearch = findViewById(R.id.main_search_btn);
       tofavorite = findViewById(R.id.main_favorite_btn);
//       recepices_btn1=findViewById(R.id.recepices_btn1);

       login=findViewById(R.id.main_login_btn);
       logout = findViewById(R.id.main_logout_btn);
       auth = FirebaseAuth.getInstance();
       user = auth.getCurrentUser();
       user_details = findViewById(R.id.user_details);

//        Button button = findViewById(R.id.main_btn1);
//        Button button2 = findViewById(R.id.main_btn2);
//        Button button3 = findViewById(R.id.main_btn3);
//        Button button4 = findViewById(R.id.main_btn4);



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


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Recipe.class);
                startActivity(intent);
            }
        };

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
//        View.OnClickListener clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Button clickedButton = (Button) view;
//                if (isFullHeart) {
//                    clickedButton.setBackgroundResource(R.drawable.ic_emptyheart);
//                } else {
//                    clickedButton.setBackgroundResource(R.drawable.ic_fullheart);
//                }
//                isFullHeart = !isFullHeart;
//            }
//        };
//        button.setOnClickListener(clickListener);
//        button2.setOnClickListener(clickListener);
//        button3.setOnClickListener(clickListener);
//        button4.setOnClickListener(clickListener);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("recipe")
                .orderBy("recipe_like", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    LinearLayout layout = findViewById(R.id.main_btnlayout); // 레이아웃의 ID를 가져옵니다.

                    int count = 0;
                    LinearLayout currentLayout = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // 버튼 생성 및 화면에 추가하는 코드 작성
                        if (count % 2 == 0) {
                            // 새로운 줄을 만듭니다.
                            currentLayout = new LinearLayout(MainActivity.this);
                            currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            currentLayout.setLayoutParams(layoutParams);
                            layout.addView(currentLayout);
                        }

                        // 버튼 생성
                        Button button = new Button(MainActivity.this);
                        String recipeName = document.getString("recipe_name");
                        button.setText(recipeName);

                        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
                        );
                        button.setLayoutParams(buttonParams);

                        currentLayout.addView(button);

                        count++;

                        // 버튼 클릭 이벤트 처리
                        button.setOnClickListener(clickListener);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private ArrayList<Integer> getIdolList() {
        ArrayList<Integer> itemList = new ArrayList<>();
        itemList.add(R.drawable.img_1);
        itemList.add(R.drawable.img_14);
        itemList.add(R.drawable.img_13);
        return itemList;
    }



}