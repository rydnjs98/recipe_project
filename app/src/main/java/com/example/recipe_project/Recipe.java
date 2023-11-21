package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {

    private TextView textView1;
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

    private ArrayList<String> recipeIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        FirebaseApp.initializeApp(this);  // Firebase 초기화

        textView1 = findViewById(R.id.textView1);
        db = FirebaseFirestore.getInstance();

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        // YouTubePlayer 초기화
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                setupDatabaseListener();
            }
        });
    }

    private void loadYouTubeVideo(String videoId) {
        if (youTubePlayer != null) {
            youTubePlayer.loadVideo(videoId, 0);
        }
    }

    private void setupDatabaseListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        listenerRegistration = db.collection("recipe").addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error != null) {
                Log.e("RecipeActivity", "데이터 읽기 실패", error);
                return;
            }

            recipeIds.clear();

            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Object recipeIdObject = documentSnapshot.getLong("recipe_ID");

                if (recipeIdObject != null) {
                    int recipeId = ((Long) recipeIdObject).intValue();
                    recipeIds.add(String.valueOf(recipeId));
                } else {
                    Log.e("RecipeActivity", "recipe_ID가 null입니다.");
                }
            }

            Log.d("RecipeActivity", "Recipe IDs: " + recipeIds.toString());

            // 서치에서 보낸 인텐트 가져오기
            Intent intent = getIntent();
            FindItem selectedItem = (FindItem) intent.getSerializableExtra("selectedItem");

            // 받은 데이터 사용
            if (selectedItem != null) {
                int selectedRecipeId = selectedItem.getRecipe_ID();

                // recipeIds에서 해당하는 레시피 ID 찾기
                int index = recipeIds.indexOf(String.valueOf(selectedRecipeId));
                if (index != -1) {
                    // 맞는 레시피 ID를 찾았을 때 해당 레시피 정보 가져오기
                    DocumentSnapshot selectedRecipe = queryDocumentSnapshots.getDocuments().get(index);
                    if (selectedRecipe != null) {
                        String recipeInfo = selectedRecipe.getString("recipe_info");

                        // recipe_ingrediantIDs를 가져올 때 타입을 확인하여 문자열로 변환
                        String recipeIngredientIDs = null;
                        Object ingredientIDsObject = selectedRecipe.get("recipe_ingrediantIDs");
                        if (ingredientIDsObject != null) {
                            if (ingredientIDsObject instanceof String) {
                                recipeIngredientIDs = (String) ingredientIDsObject;
                            } else {
                                Log.e("RecipeActivity", "recipe_ingrediantIDs의 타입이 String이 아닙니다.");
                            }
                        } else {
                            Log.e("RecipeActivity", "recipe_ingrediantIDs가 null입니다.");
                        }

                        long recipeLike = selectedRecipe.getLong("recipe_like");
                        String recipeLink = selectedRecipe.getString("recipe_link");
                        String recipeName = selectedRecipe.getString("recipe_name");
                        String recipeTag = selectedRecipe.getString("recipe_tag");

                        // 로그로 출력
                        Log.d("RecipeActivity", "Recipe Info: " + recipeInfo);
                        Log.d("RecipeActivity", "Recipe Ingredient IDs: " + recipeIngredientIDs);
                        Log.d("RecipeActivity", "Recipe Like: " + recipeLike);
                        Log.d("RecipeActivity", "Recipe Link: " + recipeLink);
                        Log.d("RecipeActivity", "Recipe Name: " + recipeName);
                        Log.d("RecipeActivity", "Recipe Tag: " + recipeTag);
                    } else {
                        Log.e("RecipeActivity", "선택된 레시피를 찾을 수 없습니다.");
                    }
                } else {
                    Log.e("RecipeActivity", "recipeIds에 선택된 레시피 ID가 없습니다.");
                }

                // 다른 필요한 작업 수행
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ListenerRegistration 해제
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
