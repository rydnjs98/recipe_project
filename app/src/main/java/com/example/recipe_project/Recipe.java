package com.example.recipe_project;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Recipe extends AppCompatActivity {

    VideoView vv;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes);

        vv = findViewById(R.id.vv);

        // Video Uri
        String recipeId = "recipe1";
        databaseReference = FirebaseDatabase.getInstance().getReference().child("0").child("recipe_link").child(recipeId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String videoUrl = dataSnapshot.getValue(String.class);
                    Uri videoUri = Uri.parse(videoUrl);

                    // URI를 로그에 출력
                    Log.d("RecipeActivity", "Video URI: " + videoUri.toString());

                    // VideoView에 동영상 설정
                    vv.setVideoURI(videoUri);

                    // 동영상 로딩 준비가 끝났을 때 재생 시작
                    vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            // 비디오 시작
                            vv.start();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RecipeActivity", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}