package com.example.recipe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView user_details;
    Button tosearch;
    Button tofavorite;
    ImageButton login;
    Button  logout;

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
        // 폰트 리소스를 가져오기
        Typeface typeface = ResourcesCompat.getFont(this, R.font.onepop);

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
        //검색 페이지 이동 버튼
        tosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, Search.class);
               startActivity(intent);
            }
        });
        // 즐겨찾기 페이지 이동 버튼
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
        
        //하트 버튼 클릭 리스너
        View.OnClickListener heartclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button clickedButton = (Button) view;
                if (isFullHeart) {
                    clickedButton.setBackgroundResource(R.drawable.ic_emptyheart);
                } else {
                    clickedButton.setBackgroundResource(R.drawable.ic_fullheart);
                }
                isFullHeart = !isFullHeart;
            }
        };

        // recipe 컬렉션에서 recipe_like를 기준으로 내림차순으로 정렬
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("recipe")
                .orderBy("recipe_like", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LinearLayout layout = findViewById(R.id.main_btnlayout); // 레이아웃의 ID를 가져옵니다.

                        int count = 0;
                            LinearLayout lineLayout = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (count % 2 == 0) {
                                    // 새로운 줄을 만듭니다.
                                    lineLayout = new LinearLayout(MainActivity.this);
                                    lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    layoutParams.setMargins(20,0,20,0);
                                    lineLayout.setLayoutParams(layoutParams);
                                    layout.addView(lineLayout);
                                }
                                
                                //weight 속성을 위해 레이아웃 추가
                                LinearLayout line2Layout;
                                line2Layout = new LinearLayout(MainActivity.this);
                                LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
                                );
                                line2Layout.setLayoutParams(linelayoutParams);
                                lineLayout.addView(line2Layout);
                                
                                // 버튼과 텍스트뷰를 겹치기 위해 프레임 레이아웃 사용
                                FrameLayout currentLayout;
                                currentLayout = new FrameLayout(MainActivity.this);
                                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(20,20,20,20);
                                currentLayout.setLayoutParams(layoutParams);
                                line2Layout.addView(currentLayout);
                                int recipeID = document.getLong("recipe_ID").intValue(); // recipe_ID 필드 값(int) 가져오기

                                // recipe_ID 값을 기반으로 이미지 리소스 ID 가져오기
                                String imageName = "recipe_" + recipeID;
                                int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setImageResource(imageResource);

                                FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.MATCH_PARENT,
                                        400
                                );
                                imageView.setLayoutParams(buttonParams);

                                currentLayout.addView(imageView);


                                // imageView 클릭했을 때의 동작
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MainActivity.this, Recipe.class);
                                        intent.putExtra("main", recipeID);
                                        startActivity(intent);
                                    }
                                });

                                Button button = new Button(MainActivity.this);
                                button.setBackgroundResource(R.drawable.heart);
                                FrameLayout.LayoutParams heartbuttonParams = new FrameLayout.LayoutParams(
                                        100,
                                        100
                                );
                                // 버튼을 이미지 뷰의 왼쪽 위에 위치하도록 설정
                                heartbuttonParams.gravity = Gravity.START | Gravity.TOP;
                                heartbuttonParams.setMargins(0, 0, 20, 20); // 버튼의 여백 설정

                                button.setOnClickListener(heartclickListener); //버튼의 클릭 리스너 설정

                                currentLayout.addView(button, heartbuttonParams); // FrameLayout에 버튼 추가

                                // textview 추가
                                TextView textView = new TextView(MainActivity.this);
                                String recipeName = document.getString("recipe_name");
                                textView.setText(recipeName);
                                FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT, 10
                                );
                                textView.setTextSize(20);
                                textView.setTypeface(typeface);
                                textView.setTextColor(Color.WHITE);
                                textParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
                                currentLayout.addView(textView, textParams);

                                count++; //2줄로 만들기 위한 변수
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