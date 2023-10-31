package com.example.recipe_project;

import static com.example.recipe_project.DataAdapter.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Start extends AppCompatActivity {
    ImageView imageKoreafood, imageChinafood, imageJapanfood, imageWesternfood;
    int ID = 21;
    String name = "밥";
    String link = "";
    String IDs = "";
    String tag = "";
    String info = "";
    DatabaseReference mPostReference = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        postFirebaseDatabase(true);



    }
    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance("https://recipe-2023-team2-default-rtdb.firebaseio.com").getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            Recipe_Post post = new Recipe_Post(ID, name, link, IDs, tag, info);
            postValues = post.toMap();
        }
        childUpdates.put("/recipe/" + ID, postValues);
        mPostReference.updateChildren(childUpdates);
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                Log.d("Database", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Database", "Failed to read value.", error.toException());
            }
        });
    }
}