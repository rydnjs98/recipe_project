package com.example.recipe_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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
import java.util.List;

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

        // Firebase 초기화
        FirebaseApp.initializeApp(this);

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
        // ListenerRegistration이 이미 존재하면 제거
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        // Firebase 데이터베이스에서 recipe_name, recipe_link을 가져와 동영상 및 텍스트 업데이트
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
                        processRecipeData(selectedRecipe);
                    } else {
                        Log.e("RecipeActivity", "선택된 레시피를 찾을 수 없습니다.");
                    }
                } else {
                    Log.e("RecipeActivity", "recipeIds에 선택된 레시피 ID가 없습니다.");
                }
            }
        });
    }

    private void processRecipeData(DocumentSnapshot recipeSnapshot) {
        String recipeInfo = recipeSnapshot.getString("recipe_info");

        // 기존 버튼들을 포함하는 레이아웃 가져오기
        LinearLayout layout2 = findViewById(R.id.layout2);

        // 기존 버튼들을 모두 제거
        layout2.removeAllViews();

        // recipe_ingrediantIDs를 가져올 때 타입을 확인하여 List<Long>으로 변환
        List<Long> recipeIngredientIDs = new ArrayList<>();
        Object ingredientIDsObject = recipeSnapshot.get("recipe_ingrediantIDs");
        if (ingredientIDsObject != null && ingredientIDsObject instanceof List) {
            List<Object> ingredientIDsList = (List<Object>) ingredientIDsObject;
            for (Object id : ingredientIDsList) {
                if (id instanceof Long) {
                    recipeIngredientIDs.add((Long) id);
                }
            }
        } else {
            Log.e("RecipeActivity", "recipe_ingrediantIDs의 타입이 List가 아니거나 null입니다.");
        }

        long recipeLike = recipeSnapshot.getLong("recipe_like");
        String recipeLink = recipeSnapshot.getString("recipe_link");
        String recipeName = recipeSnapshot.getString("recipe_name");
        String recipeTag = recipeSnapshot.getString("recipe_tag");

        // 동영상 로드 및 텍스트 업데이트
        loadYouTubeVideo(recipeLink);
        textView1.setText(recipeName);

        // 로그로 출력
        Log.d("RecipeActivity", "Recipe Info: " + recipeInfo);
        Log.d("RecipeActivity", "Recipe Ingredient IDs: " + recipeIngredientIDs);
        Log.d("RecipeActivity", "Recipe Like: " + recipeLike);
        Log.d("RecipeActivity", "Recipe Link: " + recipeLink);
        Log.d("RecipeActivity", "Recipe Name: " + recipeName);
        Log.d("RecipeActivity", "Recipe Tag: " + recipeTag);
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
