package com.example.recipe_project;

import static com.example.recipe_project.DataAdapter.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    List<Ing_post> inglist = new ArrayList<>();
    private ArrayList<String> recipeIds = new ArrayList<>();
    List<Integer> ing_id = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
//        Button btn = findViewById(R.id.btnOne);
        // Firebase 초기화
        FirebaseApp.initializeApp(this);



        textView1 = findViewById(R.id.textView1);
        db = FirebaseFirestore.getInstance();
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docref = db.collection("ingrediant");

        docref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Ing_post ing_item = doc.toObject(Ing_post.class);
                        inglist.add(ing_item);

                    }
                    for (Ing_post ing_item : inglist) {
                        Log.d(TAG, "ing name = " + ing_item.getingrediant_name());
                        Log.d(TAG, "ing link = " + ing_item.getingrediant_link());
                    }



                } else {
                    Log.d(TAG, "GET FAILED" + task.getException());
                }
            }
        });

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
    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Recipe.this, Webview.class);
            startActivity(intent);
        }
    };

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
//            Intent intent = getIntent();
//            FindItem selectedItem = (FindItem) intent.getSerializableExtra("selectedItem");
            int recipeID = getIntent().getIntExtra("recipeID",1);

            // 받은 데이터 사용
            if (recipeID != 0) {

                // recipeIds에서 해당하는 레시피 ID 찾기
                int index = recipeIds.indexOf(String.valueOf(recipeID));
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
            for(int j=0; j<recipeIngredientIDs.size(); j++){

                ing_id.add(Math.toIntExact(recipeIngredientIDs.get(j)));
                System.out.println(ing_id.get(j));
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

        LinearLayout layout = findViewById(R.id.ingrediant_layout);
        int count = 0;
        LinearLayout lineLayout = null;
        List<Object> ingredientIDsList = (List<Object>) ingredientIDsObject;
            for (Object id : ingredientIDsList) {
                if (count % 3 == 0) {
                    // 새로운 줄을 만듭니다.
                    lineLayout = new LinearLayout(Recipe.this);
                    lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,1
                    );
                    layoutParams.setMargins(15,0,15,0);
                    lineLayout.setLayoutParams(layoutParams);
                    layout.addView(lineLayout);
                }
                String imageName = "i_" + id;
                int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
                ImageView imageView = new ImageView(Recipe.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(imageResource);

                // 화면 가로 크기 구하기
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;

                // 버튼의 너비를 화면 가로 크기의 1/3로 설정하여 정사각형으로 만듭니다.
                int buttonSize = (screenWidth / 3) - 50;
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        buttonSize,
                        buttonSize
                );
                buttonParams.setMargins(20,20,20,20);
                imageView.setBackgroundColor(Color.BLACK);
                imageView.setLayoutParams(buttonParams);



                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Ing_post> ing_link= new ArrayList<>();
                        for(int i =0 ; i<inglist.size(); i++){
                            if(inglist.get(i).ingrediant_ID == (int) (long) id){
                                ing_link.add(inglist.get(i));
                            }

                        }


                        Intent intent = new Intent(Recipe.this, Webview.class);
                        intent.putExtra("Ing_link", ing_link.get(0).ingrediant_link );
                        startActivity(intent);
                    }
                });





                lineLayout.addView(imageView);
                Log.d("RecipeActivity", "id: " + id);
                count++;
            }

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
