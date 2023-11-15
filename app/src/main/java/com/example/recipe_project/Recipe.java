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
    private String videoId = "0bnFoRQebq0"; // YouTube 동영상의 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        FirebaseApp.initializeApp(this);  // Firebase 초기화

        textView1 = findViewById(R.id.textView1);
        db = FirebaseFirestore.getInstance();

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        // YouTubePlayer 초기화 및 동영상 로딩
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0); // 동영상 ID와 시작 시간 (0초) 지정
            }
        });

        // Firebase 데이터베이스에서 recipe_name을 가져와 textView1에 할당
        listenerRegistration = db.collection("recipe").document("QBadFEdMEXQ8fkUPK5BI").addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.e("RecipeActivity", "데이터 읽기 실패", error);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                String recipeName = documentSnapshot.getString("recipe_name");
                if (recipeName != null) {
                    Log.d("RecipeActivity", "onEvent 호출됨");
                    textView1.setText(recipeName);
                } else {
                    Log.e("RecipeActivity", "레시피 이름이 null입니다");
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
