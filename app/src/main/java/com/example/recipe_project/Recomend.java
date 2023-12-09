package com.example.recipe_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recomend extends AppCompatActivity {



    FirebaseAuth auth;
    FirebaseUser user;
    Button login;
    TextView user_details;
    Button tosearch;
    Button tofavorite, tomain;

    Button torecommend;
    Button  logout;

    List<String> stag = new ArrayList<>();;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomend);
        login=findViewById(R.id.main_login_btn);
        logout = findViewById(R.id.main_logout_btn);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_details = findViewById(R.id.user_details);
        tosearch = findViewById(R.id.frypan_search_btn);
        tofavorite = findViewById(R.id.frypan_favorite_btn);
        tomain = findViewById(R.id.frypan_main_btn);
        torecommend = findViewById(R.id.frypan_frypan);



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


        tosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recomend.this, Search.class);
                startActivity(intent);
            }
        });
        // 즐겨찾기 페이지 이동 버튼
        tofavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null) {
                    Intent intent = new Intent(Recomend.this, Favorite.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Recomend.this, "로그인을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        torecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Recomend.this, Recomend.class);
                startActivity(intent);


            }
        });

        tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Recomend.this, MainActivity.class);
                startActivity(intent);


            }
        });


        init(null, null);
    }

    public void init(List<Integer> resultID, String selectedtag) {
        Typeface typeface = ResourcesCompat.getFont(this, R.font.onepop);
        if(selectedtag == null){
            db = FirebaseFirestore.getInstance();
            stag.clear();

            // recipe_tag document에서 데이터 가져오기
            CollectionReference recipeCollection = db.collection("recipe");

            recipeCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Set<String> uniqueTags = new HashSet<>(); // 중복 없이 태그를 저장할 Set

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                String tags = document.getString("recipe_tag");
                                if (tags != null && !tags.isEmpty()) {
                                    // 쉼표(,)로 구분된 태그들을 분리하여 Set에 추가
                                    String[] tagArray = tags.split(",");
                                    uniqueTags.addAll(Arrays.asList(tagArray));
                                }
                            }
                        }

                        // 중복 없는 태그들이 저장된 Set을 배열로 변환
                        String[] uniqueTagsArray = uniqueTags.toArray(new String[0]);
                        // 변환된 배열 출력
                        for (int i = 0; i < uniqueTagsArray.length; i++) {
                           // Log.d("tag", uniqueTagsArray[i]);
                        }
                        LinearLayout layout = findViewById(R.id.recomend_btnlayout); // 레이아웃의 ID를 가져옵니다.

                        int count = 0;
                        LinearLayout lineLayout = null;
                        for (int i = 0; i < uniqueTagsArray.length; i++) {
                            String selecttag = uniqueTagsArray[i]; //선택한 태그
                            if (count % 3 == 0) {
                                // 새로운 줄을 만듭니다.
                                lineLayout = new LinearLayout(Recomend.this);
                                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT, 1
                                );
                                layoutParams.setMargins(20, 0, 20, 0);
                                lineLayout.setLayoutParams(layoutParams);
                                layout.addView(lineLayout);
                            }

                            //weight 속성을 위해 레이아웃 추가
                            LinearLayout line2Layout;
                            line2Layout = new LinearLayout(Recomend.this);
                            LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            line2Layout.setLayoutParams(linelayoutParams);
                            lineLayout.addView(line2Layout);

                            // 버튼과 텍스트뷰를 겹치기 위해 프레임 레이아웃 사용
                            FrameLayout currentLayout;
                            currentLayout = new FrameLayout(Recomend.this);
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.WRAP_CONTENT
                            );
                            currentLayout.setBackgroundResource(R.drawable.round);
                            currentLayout.setLayoutParams(layoutParams);
                            line2Layout.addView(currentLayout);

                            Button button = new Button(Recomend.this);

                            // 화면 가로 크기 구하기
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int screenWidth = displayMetrics.widthPixels;

                            // 버튼의 너비를 화면 가로 크기의 1/3로 설정하여 정사각형으로 만듭니다.
                            int buttonSize = (screenWidth / 3) - 50;
                            FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                                    buttonSize,
                                    buttonSize
                            );
                            button.setText(uniqueTagsArray[i]);


                            buttonParams.setMargins(20, 20, 20, 20);
//                        //button.setBackgroundColor(Color.BLACK);
                            button.setLayoutParams(buttonParams);
                            currentLayout.addView(button);
                            button.setBackgroundResource(R.drawable.rec_rounded_background);
                            button.setTextSize(20);


                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                layout.removeAllViews();
                                stag.add(selecttag);
                                init(null, selecttag);
                                }
                            });

                            count++;
                        }
                    } else {
                        Log.d("tag", "Error getting documents: ", task.getException());
                    }
                }
            });
        } else if (resultID == null){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference recipeCollection = db.collection("recipe");

                // 해당 태그를 포함하는 recipe를 쿼리하기 위한 쿼리 생성
                Query query = recipeCollection;

                query = query.whereArrayContains("recipe_tags", selectedtag);



                // 쿼리 실행
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Set<String> uniqueTags = new HashSet<>(); // 중복 없이 태그를 저장할 Set
                            if (task.getResult().size() == 1) { // 결과의 크기가 1인 경우
                                DocumentSnapshot document2 = task.getResult().getDocuments().get(0);
                                if (document2.exists()) {
                                    // 결과가 하나일 때 "결과!" 출력
                                   // Log.d("결과", "결과!" + document2.getId() + document2.getData());

                                    LinearLayout layout = findViewById(R.id.recomend_btnlayout);

                                    // 결과 textview 추가
                                    TextView textView = new TextView(Recomend.this);
                                    textView.setText("선택한 태그 : " + stag);
                                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT, 10
                                    );
                                    textView.setTextSize(20);
                                    textView.setTypeface(typeface);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                    layout.addView(textView, textParams);

                                    TextView textView2 = new TextView(Recomend.this);
                                    String recipeName = document2.getString("recipe_name");
                                    textView2.setText(recipeName);
                                    LinearLayout.LayoutParams text2Params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT, 10
                                    );
                                    textView2.setTextSize(20);
                                    textView2.setTypeface(typeface);
                                    textView2.setTextColor(Color.BLACK);
                                    textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                                    layout.addView(textView2, text2Params);

                                    // 결과 이미지뷰 출력
                                    int recipeID = document2.getLong("recipe_ID").intValue();
                                    // recipe_ID 값을 기반으로 이미지 리소스 ID 가져오기
                                    String imageName = "recipe_" + recipeID;
                                    int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                    ImageView imageView = new ImageView(Recomend.this);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    imageView.setBackgroundResource(R.drawable.rounded_background);


                                    imageView.setImageResource(imageResource);

                                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            600
                                    );
                                    imageView.setLayoutParams(buttonParams);


                                    layout.addView(imageView);


                                    // imageView 클릭했을 때의 동작
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Recomend.this, Recipe.class);
                                            intent.putExtra("recipeID", recipeID);
                                            startActivity(intent);
                                        }
                                    });

                                    //결과 처음으로가기 버튼
                                    LinearLayout currentLayout;
                                    currentLayout = new LinearLayout(Recomend.this);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                    );
                                    currentLayout.setLayoutParams(layoutParams);
                                    layout.addView(currentLayout);

                                    Button button = new Button(Recomend.this);

                                    button.setText("처음으로");
                                    LinearLayout.LayoutParams button2Params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    button2Params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            layout.removeAllViews();
                                            init(null,null);
                                        }
                                    });

                                    currentLayout.addView(button, button2Params);
                                }
                            } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            String tags = document.getString("recipe_tag");
                                            if (tags != null && !tags.isEmpty()) {
                                                // 쉼표(,)로 구분된 태그들을 분리하여 Set에 추가
                                                String[] tagArray = tags.split(",");
                                                uniqueTags.addAll(Arrays.asList(tagArray));
                                            }
                                        }
                                    }

                                    // 중복 없는 태그들이 저장된 Set을 배열로 변환
                                    String[] uniqueTagsArray = uniqueTags.toArray(new String[0]);
                                    for (int i = 0; i < uniqueTagsArray.length; i++) {
                                      //  Log.d("tag", uniqueTagsArray[i]);
                                    }
                                    LinearLayout layout = findViewById(R.id.recomend_btnlayout); // 레이아웃의 ID를 가져옵니다.

                                    int count = 0;
                                    LinearLayout lineLayout = null;
                                    for (int i = 0; i < uniqueTagsArray.length; i++) {
                                        String selecttag = uniqueTagsArray[i];
                                        List<Integer> result = new ArrayList<Integer>();
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                        int recipeID = document.getLong("recipe_ID").intValue();
                                        if(uniqueTagsArray[i] != selectedtag) {
                                            if (count % 3 == 0) {
                                                // 새로운 줄을 만듭니다.
                                                lineLayout = new LinearLayout(Recomend.this);
                                                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT, 1
                                                );
                                                layoutParams.setMargins(20, 0, 20, 0);
                                                lineLayout.setLayoutParams(layoutParams);
                                                layout.addView(lineLayout);
                                            }

                                            //weight 속성을 위해 레이아웃 추가
                                            LinearLayout line2Layout;
                                            line2Layout = new LinearLayout(Recomend.this);
                                            LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            );
                                            line2Layout.setLayoutParams(linelayoutParams);
                                            lineLayout.addView(line2Layout);

                                            // 버튼과 텍스트뷰를 겹치기 위해 프레임 레이아웃 사용
                                            FrameLayout currentLayout;
                                            currentLayout = new FrameLayout(Recomend.this);
                                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                                    FrameLayout.LayoutParams.WRAP_CONTENT
                                            );
                                            currentLayout.setBackgroundResource(R.drawable.round);
                                            currentLayout.setLayoutParams(layoutParams);
                                            line2Layout.addView(currentLayout);

                                            Button button = new Button(Recomend.this);

                                            // 화면 가로 크기 구하기
                                            DisplayMetrics displayMetrics = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                            int screenWidth = displayMetrics.widthPixels;

                                            // 버튼의 너비를 화면 가로 크기의 1/3로 설정하여 정사각형으로 만듭니다.
                                            int buttonSize = (screenWidth / 3) - 50;
                                            FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                                                    buttonSize,
                                                    buttonSize
                                            );
                                            button.setText(uniqueTagsArray[i]);
                                            buttonParams.setMargins(20, 20, 20, 20);
                                            button.setLayoutParams(buttonParams);
                                            currentLayout.addView(button);
                                            button.setBackgroundResource(R.drawable.rec_rounded_background);
                                            button.setTextSize(20);

                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    layout.removeAllViews();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        result.add(document.getLong("recipe_ID").intValue());
                                                    }
                                                    //   Log.d("tag2", result.toString());
                                                    stag.add(selecttag);
                                                    init(result, selecttag);
                                                }
                                            });

                                            count++;
                                        }
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if (document.exists()) {
                                            // 검색된 recipe 처리 (여기에서는 document.getData() 등을 활용하여 처리할 수 있습니다.)
                                       //     Log.d("tag", document.getId() + " => " + document.get("recipe_tags"));
                                        }
                                    }
                                }

                            }else{
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference recipeCollection = db.collection("recipe");

            // 해당 태그를 포함하는 recipe를 쿼리하기 위한 쿼리 생성
            Query query = recipeCollection;

            query = query.whereIn("recipe_ID", resultID).whereArrayContains("recipe_tags", selectedtag);

            // 쿼리 실행
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Set<String> uniqueTags = new HashSet<>(); // 중복 없이 태그를 저장할 Set
                        if (task.getResult().size() == 1) { // 결과의 크기가 1인 경우
                            DocumentSnapshot document2 = task.getResult().getDocuments().get(0);
                            if (document2.exists()) {
                                // 결과가 하나일 때 "결과!" 출력
                             //   Log.d("결과", "결과!" + document2.getId() + document2.getData());
                                LinearLayout layout = findViewById(R.id.recomend_btnlayout);

                                // 결과 textview 추가
                                TextView textView = new TextView(Recomend.this);
                                textView.setText("선택한 태그 : " + stag);
                                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT, 10
                                );
                                textView.setTextSize(20);
                                textView.setTypeface(typeface);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                layout.addView(textView, textParams);

                                TextView textView2 = new TextView(Recomend.this);
                                String recipeName = document2.getString("recipe_name");
                                textView2.setText(recipeName);
                                LinearLayout.LayoutParams text2Params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT, 10
                                );
                                textView2.setTextSize(20);
                                textView2.setTypeface(typeface);
                                textView2.setTextColor(Color.BLACK);
                                textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                                layout.addView(textView2, text2Params);

                                //결과 이미지뷰 출력

                                int recipeID = document2.getLong("recipe_ID").intValue();
                                // recipe_ID 값을 기반으로 이미지 리소스 ID 가져오기
                                String imageName = "recipe_" + recipeID;
                                int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                ImageView imageView = new ImageView(Recomend.this);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setBackgroundResource(R.drawable.rounded_background);


                                imageView.setImageResource(imageResource);

                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        600
                                );
                                imageView.setLayoutParams(buttonParams);

                                layout.addView(imageView);


                                // imageView 클릭했을 때의 동작
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Recomend.this, Recipe.class);
                                        intent.putExtra("recipeID", recipeID);
                                        startActivity(intent);
                                    }
                                });

                                //결과 처음으로가기 버튼
                                LinearLayout currentLayout;
                                currentLayout = new LinearLayout(Recomend.this);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                currentLayout.setLayoutParams(layoutParams);
                                layout.addView(currentLayout);

                                Button button = new Button(Recomend.this);

                                button.setText("처음으로");
                                LinearLayout.LayoutParams button2Params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                button2Params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layout.removeAllViews();
                                        init(null,null);
                                    }
                                });

                                currentLayout.addView(button, button2Params);

                            }
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    String tags = document.getString("recipe_tag");
                                    if (tags != null && !tags.isEmpty()) {
                                        // 쉼표(,)로 구분된 태그들을 분리하여 Set에 추가
                                        String[] tagArray = tags.split(",");
                                        uniqueTags.addAll(Arrays.asList(tagArray));
                                    }
                                }
                            }

                            // 중복 없는 태그들이 저장된 Set을 배열로 변환
                            String[] uniqueTagsArray = uniqueTags.toArray(new String[0]);
                            for (int i = 0; i < uniqueTagsArray.length; i++) {
                            //    Log.d("tag", uniqueTagsArray[i]);
                            }
                            LinearLayout layout = findViewById(R.id.recomend_btnlayout); // 레이아웃의 ID를 가져옵니다.

                            int count = 0;
                            LinearLayout lineLayout = null;
                            for (int i = 0; i < uniqueTagsArray.length; i++) {
                                String selecttag = uniqueTagsArray[i];
                                List<Integer> result = new ArrayList<Integer>();
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                int recipeID = document.getLong("recipe_ID").intValue();
                                if(stag.contains(uniqueTagsArray[i]) != true){
                                    if (count % 3 == 0) {
                                        // 새로운 줄을 만듭니다.
                                        lineLayout = new LinearLayout(Recomend.this);
                                        lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT, 1
                                        );
                                        layoutParams.setMargins(20, 0, 20, 0);
                                        lineLayout.setLayoutParams(layoutParams);
                                        layout.addView(lineLayout);
                                    }

                                    //weight 속성을 위해 레이아웃 추가
                                    LinearLayout line2Layout;
                                    line2Layout = new LinearLayout(Recomend.this);
                                    LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    line2Layout.setLayoutParams(linelayoutParams);
                                    lineLayout.addView(line2Layout);

                                    // 버튼과 텍스트뷰를 겹치기 위해 프레임 레이아웃 사용
                                    FrameLayout currentLayout;
                                    currentLayout = new FrameLayout(Recomend.this);
                                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                            FrameLayout.LayoutParams.MATCH_PARENT,
                                            FrameLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    currentLayout.setBackgroundResource(R.drawable.round);
                                    currentLayout.setLayoutParams(layoutParams);
                                    line2Layout.addView(currentLayout);

                                    Button button = new Button(Recomend.this);

                                    // 화면 가로 크기 구하기
                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                    int screenWidth = displayMetrics.widthPixels;

                                    // 버튼의 너비를 화면 가로 크기의 1/3로 설정하여 정사각형으로 만듭니다.
                                    int buttonSize = (screenWidth / 3) - 50;
                                    FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                                            buttonSize,
                                            buttonSize
                                    );
                                    button.setText(uniqueTagsArray[i]);
                                    buttonParams.setMargins(20, 20, 20, 20);
                                    button.setLayoutParams(buttonParams);
                                    currentLayout.addView(button);
                                    button.setBackgroundResource(R.drawable.rec_rounded_background);
                                    button.setTextSize(20);

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            layout.removeAllViews();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                result.add(document.getLong("recipe_ID").intValue());
                                            }
                                            stag.add(selecttag);
                                            init(result, selecttag);
                                        }
                                    });

                                    count++;
                                }
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.exists()) {
                                    // 검색된 recipe 처리 (여기에서는 document.getData() 등을 활용하여 처리할 수 있습니다.)
                               //     Log.d("tag", document.getId() + " => " + document.get("recipe_tags"));
                                }
                            }
                        }

                    }else{
                        Log.d("tag", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }
}
