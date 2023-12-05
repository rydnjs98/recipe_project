package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                                LinearLayout layout = findViewById(R.id.Favirute_layout); // 레이아웃의 ID를 가져옵니다.

                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + "=>" + document.getData());
                                    String documentName = document.getId();
//                                    Log.d(TAG, document.getId() + "=>" + document.getId());
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
                                                            LinearLayout currentLayout = null;

                                                            currentLayout = new LinearLayout(Favorite.this);
                                                            currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                                                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                                            );
                                                            currentLayout.setLayoutParams(layoutParams);


                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                Log.d(TAG, document.getId() + "=>" + document.getData().get("recipe_name"));
                                                                String recipeName = document.getString("recipe_name");

                                                                int recipeID = document.getLong("recipe_ID").intValue();

                                                                //레이아웃 동적생성

                                                                layout.addView(currentLayout);
                                                                // 버튼 생성
                                                                ImageView imageView = new ImageView(Favorite.this);
                                                                TextView textView = new TextView(Favorite.this);
                                                                Button LikeButton = new Button((Favorite.this));

                                                                textView.setText(recipeName);
                                                                textView.setTextSize(20);
                                                                textView.setGravity(1);

                                                                LikeButton.setBackgroundResource(R.drawable.ic_fullheart);

                                                                String imageName = "recipe_" + recipeID;
                                                                int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                                                imageView.setImageResource(imageResource);
                                                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                                                LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(300, 300);
                                                                imageViewParams.setMargins(100, 0, 0, 40);

                                                                imageView.setLayoutParams(imageViewParams);


                                                                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                                                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                                                        300
                                                                );

                                                                FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                                                                        100,
                                                                        100
                                                                );

                                                                textViewParams.setMargins(70, 0, 0, 0);

                                                                imageView.setLayoutParams(imageViewParams);
                                                                textView.setLayoutParams(textViewParams);
                                                                LikeButton.setLayoutParams(buttonParams);

                                                                currentLayout.addView(imageView);
                                                                currentLayout.addView(LikeButton);
                                                                currentLayout.addView(textView);


                                                                // 이미지뷰 클릭 리스너
                                                                imageView.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Intent intent = new Intent(Favorite.this, Recipe.class);
                                                                        intent.putExtra("recipeID", recipeID);
                                                                        startActivity(intent);
                                                                    }
                                                                });

                                                                LinearLayout finalCurrentLayout = currentLayout;


                                                                //하트 버튼 클릭시 리스트 삭제
                                                                LikeButton.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        db.collection("favorite")
                                                                                .document(documentName)
                                                                                .update("recipe_ID", FieldValue.arrayRemove(recipeID));

                                                                        db.collection("recipe")
                                                                                .whereEqualTo("recipe_ID", recipeID)
                                                                                .get()
                                                                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                                                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                                                                    Long r_like =  documentSnapshot.getLong("recipe_like") - 1;
                                                                                    Map<String, Object> updateData = new HashMap<>();
                                                                                    updateData.put("recipe_like", r_like);
                                                                                    documentSnapshot.getReference().set(updateData, SetOptions.merge())
                                                                                            .addOnSuccessListener(aVoid -> {
                                                                                                // 업데이트 성공 시 실행되는 부분
                                                                                            })
                                                                                            .addOnFailureListener(e -> {
                                                                                                // 업데이트 실패 시 실행되는 부분
                                                                                                // 에러 메시지 등을 처리할 수 있습니다.
                                                                                            });
                                                                                });

                                                                        if (finalCurrentLayout != null) {
                                                                            ((LinearLayout) finalCurrentLayout.getParent()).removeView(finalCurrentLayout);
                                                                        }
                                                                    }
                                                                });
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
                finish();
            }
        });
        tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorite.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tofavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorite.this, Favorite.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
