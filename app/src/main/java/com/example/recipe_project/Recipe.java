package com.example.recipe_project;

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

public class Recipe extends AppCompatActivity {

    private TextView textView1;
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

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
                youTubePlayer = player; // YouTubePlayer 인스턴스 저장
                loadYouTubeVideo("YOUR_VIDEO_ID");
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
        listenerRegistration = db.collection("recipe").document("QBadFEdMEXQ8fkUPK5BI").addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.e("RecipeActivity", "데이터 읽기 실패", error);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                String recipeName = documentSnapshot.getString("recipe_name");
                String videoId = documentSnapshot.getString("recipe_link");

                if (recipeName != null && videoId != null) {
                    Log.d("RecipeActivity", "onEvent 호출");
                    textView1.setText(recipeName);
                    loadYouTubeVideo(videoId);
                } else {
                    Log.e("RecipeActivity", "레시피 이름 또는 동영상 ID가 null");
                }
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
