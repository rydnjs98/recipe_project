package com.example.recipe_project;



import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Recipe extends AppCompatActivity {

    private TextView textView1, textView4;
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    List<Ing_post> inglist = new ArrayList<>();
    private ArrayList<String> recipeIds = new ArrayList<>();

    Button heartButton;
    private FirebaseUser user;
    private TextView like_num;


    private boolean isFullHeart = false; //빈 하트
    List<Integer> ing_id = new ArrayList<>();
    private List<Integer> selectedImageIds = new ArrayList<>();//이미 선택된 이미지id저장


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
//        Button btn = findViewById(R.id.btnOne);
        // Firebase 초기화
//        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        int recipeID = getIntent().getIntExtra("recipeID", -1);

        heartButton = findViewById(R.id.recipe_heart);

        // 찜하기 상태 초기화
        checkIfFavorite(recipeID);

        setRandomImageToView((RelativeLayout) findViewById(R.id.Recipe_reco1));
        setRandomImageToView((RelativeLayout) findViewById(R.id.Recipe_reco2));
        setRandomImageToView((RelativeLayout) findViewById(R.id.Recipe_reco3));
        setRandomImageToView((RelativeLayout) findViewById(R.id.Recipe_reco4));


        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null || user.getEmail() == null) {
                    Toast.makeText(Recipe.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String u_name = user.getEmail();
                int recipeID = getIntent().getIntExtra("recipeID", -1);

                if (isFullHeart) {
                    removeRecipeIdFromDocument(u_name, recipeID);
                    heartButton.setBackgroundResource(R.drawable.ic_emptyheart);
                } else {
                    addDataToFirestore(recipeID, u_name);
                    heartButton.setBackgroundResource(R.drawable.ic_fullheart);
                }
                isFullHeart = !isFullHeart;
            }
        });

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
                      //  Log.d(TAG, "ing name = " + ing_item.getingrediant_name());
                      //  Log.d(TAG, "ing link = " + ing_item.getingrediant_link());
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
            youTubePlayer.cueVideo(videoId, 0);
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

           // Log.d("RecipeActivity", "Recipe IDs: " + recipeIds.toString());

            // 서치에서 보낸 인텐트 가져오기
//            Intent intent = getIntent();
//            FindItem selectedItem = (FindItem) intent.getSerializableExtra("selectedItem");
            int recipeID = getIntent().getIntExtra("recipeID", 1);

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
        textView4 = findViewById(R.id.textView4);
        textView4.setText(recipeInfo);
//        textView4.setMovementMethod(new ScrollingMovementMethod());
        Typeface typeface = ResourcesCompat.getFont(this, R.font.onepop);
        long recipeLike = recipeSnapshot.getLong("recipe_like");
        like_num = findViewById(R.id.like_num);
        like_num.setText(String.valueOf(recipeLike)); // 좋아요 수를 표시합니다.


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
            for (int j = 0; j < recipeIngredientIDs.size(); j++) {

                ing_id.add(Math.toIntExact(recipeIngredientIDs.get(j)));
                System.out.println(ing_id.get(j));
            }
        } else {
            Log.e("RecipeActivity", "recipe_ingrediantIDs의 타입이 List가 아니거나 null입니다.");
        }


        String recipeLink = recipeSnapshot.getString("recipe_link");
        String recipeName = recipeSnapshot.getString("recipe_name");
        String recipeTag = recipeSnapshot.getString("recipe_tag");

        // 동영상 로드 및 텍스트 업데이트
        loadYouTubeVideo(recipeLink);
        textView1.setText(recipeName);

        LinearLayout layout = findViewById(R.id.ingrediant_layout);
        layout.removeAllViews();

        int count = 0;
        LinearLayout lineLayout = null;
        List<Object> ingredientIDsList = (List<Object>) ingredientIDsObject;
        for (Object id : ingredientIDsList) {
            if (count % 3 == 0) {
                // 새로운 줄을 만듭니다.
                lineLayout = new LinearLayout(Recipe.this);
                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1
                );
                layoutParams.setMargins(15, 0, 15, 0);
                lineLayout.setLayoutParams(layoutParams);
                layout.addView(lineLayout);
            }

            //weight 속성을 위해 레이아웃 추가
            LinearLayout line2Layout;
            line2Layout = new LinearLayout(Recipe.this);
            LinearLayout.LayoutParams linelayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            line2Layout.setLayoutParams(linelayoutParams);
            lineLayout.addView(line2Layout);

            // 버튼과 텍스트뷰를 겹치기 위해 프레임 레이아웃 사용
            FrameLayout currentLayout;
            currentLayout = new FrameLayout(Recipe.this);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
//            layoutParams.setMargins(20,20,20,20);
            currentLayout.setLayoutParams(layoutParams);
            line2Layout.addView(currentLayout);

            //재료 imageView 추가
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
            FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                    buttonSize,
                    buttonSize
            );
            buttonParams.setMargins(20, 20, 20, 20);
            //imageView.setBackgroundColor(Color.BLACK);
            imageView.setLayoutParams(buttonParams);
//            imageView.setPadding(20,20,20,20);
            imageView.setBackgroundResource(R.drawable.recipe_rounded_background);
            imageView.setClipToOutline(true);
            currentLayout.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Ing_post> ing_link = new ArrayList<>();
                    for (int i = 0; i < inglist.size(); i++) {
                        if (inglist.get(i).ingrediant_ID == (int) (long) id) {
                            ing_link.add(inglist.get(i));
                        }

                    }


                    Intent intent = new Intent(Recipe.this, Webview.class);
                    intent.putExtra("Ing_link", ing_link.get(0).ingrediant_link);
                    startActivity(intent);
                }
            });


            // textview 추가
            TextView textView = new TextView(Recipe.this);
            db.collection("ingrediant")
                    .whereEqualTo("ingrediant_ID", id)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String ingrediant_name = documentSnapshot.getString("ingrediant_name");
                        textView.setText(ingrediant_name);
                    });
            FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, 10
            );
            textView.setTextSize(20);
            textView.setTypeface(typeface);
            textView.setTextColor(Color.WHITE);
            textView.setShadowLayer(15, 0, 0, Color.BLACK);
            textParams.gravity = Gravity.BOTTOM | Gravity.CENTER;


            currentLayout.addView(textView, textParams);

           // Log.d("RecipeActivity", "id: " + id);
            count++;
        }

        // 로그로 출력
      //  Log.d("RecipeActivity", "Recipe Info: " + recipeInfo);
      //  Log.d("RecipeActivity", "Recipe Ingredient IDs: " + recipeIngredientIDs);
      //  Log.d("RecipeActivity", "Recipe Like: " + recipeLike);
     //   Log.d("RecipeActivity", "Recipe Link: " + recipeLink);
      //  Log.d("RecipeActivity", "Recipe Name: " + recipeName);
       // Log.d("RecipeActivity", "Recipe Tag: " + recipeTag);
        int recipeID = getIntent().getIntExtra("recipeID", 1);
      //  Log.d("RecipeActivity", "Recipe ID: " + recipeID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ListenerRegistration 해제
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private boolean isUserLoggedIn() {// 사용자가 로그인한 상태인지 확인
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null;
    }

    public void addDataToFirestore(int r_id, String u_name) {
        // 데이터를 저장할 Map 생성
        Map<String, Object> data = new HashMap<>();
        data.put("recipe_ID", r_id);
        data.put("user_id", u_name);
        // 필요한 만큼의 필드를 추가

        db.collection("favorite")
                .whereEqualTo("user_id", u_name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // 문서가 없으면 새로운 문서를 추가
                        Map<String, Object> newDocumentData = new HashMap<>();
                        newDocumentData.put("user_id", u_name);
                        newDocumentData.put("recipe_ID", Arrays.asList(r_id));

                        // 'set()' 메서드를 사용하여 새로운 문서 추가
                        db.collection("favorite")
                                .document()
                                .set(newDocumentData)
                                .addOnSuccessListener(aVoid -> {
                                    // 문서 추가 성공 시 실행되는 부분
                                })
                                .addOnFailureListener(e -> {
                                    // 문서 추가 실패 시 실행되는 부분
                                    // 에러 메시지 등을 처리할 수 있습니다.
                                });
                    } else {
                        // 문서가 이미 존재하면 기존 문서를 업데이트
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        List<Integer> recipeIds = (List<Integer>) documentSnapshot.get("recipe_ID");

                        // 새로운 값 추가
                        if (recipeIds == null) {
                            recipeIds = new ArrayList<>();
                        }
                        recipeIds.add(r_id);

                        // 업데이트할 데이터 생성
                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("recipe_ID", recipeIds);

                        // 'set()' 메서드를 사용하여 문서 업데이트 (기존 문서를 덮어쓰게 됨)
                        documentSnapshot.getReference().set(updateData, SetOptions.merge())
                                .addOnSuccessListener(aVoid -> {
                                    // 업데이트 성공 시 실행되는 부분
                                })
                                .addOnFailureListener(e -> {
                                    // 업데이트 실패 시 실행되는 부분
                                    // 에러 메시지 등을 처리할 수 있습니다.
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // 문서 가져오기 실패 시 실행되는 부분
                    // 에러 메시지 등을 처리할 수 있습니다.
                });
        // recipe_like 증가
        db.collection("recipe")
                .whereEqualTo("recipe_ID", r_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    Long r_like = documentSnapshot.getLong("recipe_like") + 1;
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
    }

    public void removeRecipeIdFromDocument(String u_name, int r_id) {
        // 찜 목록에서 레시피 제거
        db.collection("favorite")
                .whereEqualTo("user_id", u_name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        List<Long> recipeIds = (List<Long>) documentSnapshot.get("recipe_ID");

                        if (recipeIds != null && recipeIds.contains((long) r_id)) {
                            recipeIds.remove((Long) (long) r_id);

                            documentSnapshot.getReference().update("recipe_ID", recipeIds)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "레시피 찜 목록에서 제거 성공");
                                        heartButton.setBackgroundResource(R.drawable.ic_emptyheart);
                                        isFullHeart = false;

                                        // recipe_like 카운트 감소
                                        decrementRecipeLike(r_id);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "레시피 찜 목록에서 제거 실패", e);
                                    });
                        }
                    } else {
                        Log.d(TAG, "사용자의 찜 목록이 존재하지 않습니다.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "문서 가져오기 실패", e);
                });
    }

    private void decrementRecipeLike(int r_id) {
        db.collection("recipe")
                .whereEqualTo("recipe_ID", r_id)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot recipeDocument = queryDocumentSnapshots.getDocuments().get(0);
                        Long recipeLike = recipeDocument.getLong("recipe_like");
                        if (recipeLike != null && recipeLike > 0) {
                            recipeDocument.getReference().update("recipe_like", recipeLike - 1)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "recipe_like 감소 성공"))
                                    .addOnFailureListener(e -> Log.e(TAG, "recipe_like 감소 실패", e));
                        }
                    }
                });
    }

    private void checkIfFavorite(int recipeID) {
        // 사용자가 로그인한 상태인지 확인
        if (user == null) {
            return;
        }

        String userEmail = user.getEmail();
        db.collection("favorite")
                .whereEqualTo("user_id", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean isFavorited = false;
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        List<Long> userFavorites = (List<Long>) document.get("recipe_ID");
                        if (userFavorites != null && userFavorites.contains((long) recipeID)) {
                            isFavorited = true;
                            break;
                        }
                    }

                    isFullHeart = isFavorited;
                    heartButton.setBackgroundResource(isFavorited ? R.drawable.ic_fullheart : R.drawable.ic_emptyheart);
                })
                .addOnFailureListener(e -> Log.e(TAG, "즐겨찾기 확인 오류", e));
    }

    private void setRandomImageToView(RelativeLayout layout) {
        ImageView imageView = new ImageView(this);
        int imageResId = getRandomImageResId();
        imageView.setImageResource(imageResId);
        imageView.setTag(imageResId); // 이미지 리소스 ID를 태그로 저장
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundResource(R.drawable.recipe_rounded_background);
        imageView.setClipToOutline(true);


        layout.addView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        imageView.setOnClickListener(v -> {
            int clickedImageResId = (Integer) v.getTag(); // 태그에서 이미지 리소스 ID를 가져옴
            int recipeId = mapImageResIdToRecipeId(clickedImageResId);

            Intent retent = new Intent(Recipe.this,Recipe.class);
            retent.putExtra("recipeID", recipeId);
            startActivity(retent);


            //loadRecipeDetails(recipeId);
        });
    }

    private int getRandomImageResId() {
        Random random = new Random();
        int randomNumber;

        do {
            randomNumber = random.nextInt(20) + 1;
        } while (selectedImageIds.contains(randomNumber)); // 이미 선택된 이미지가 아닐 때까지 반복

        selectedImageIds.add(randomNumber); // 선택된 이미지 ID를 리스트에 추가

        String imageName = "img_" + randomNumber;
        return getResources().getIdentifier(imageName, "drawable", getPackageName());
    }


    private int mapImageResIdToRecipeId(int imageResId) {
        Resources resources = getResources();
        String resourceName = resources.getResourceEntryName(imageResId);
        String numberPart = resourceName.substring(4); // "img_" 다음 부분을 추출
        return Integer.parseInt(numberPart); // 숫자 부분을 정수로 변환
    }

    private void loadRecipeDetails(int recipeId) {
      //  Log.d("RecipeActivity", "Selected Recipe ID: " + recipeId);

        // Firestore에서 특정 recipe_ID에 해당하는 문서 검색
        db.collection("recipe")
                .whereEqualTo("recipe_ID", recipeId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        processRecipeData(documentSnapshot); // 레시피 데이터 처리
                    } else {
                        Toast.makeText(Recipe.this, "레시피 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("RecipeActivity", "Error fetching recipe details", e);
                    Toast.makeText(Recipe.this, "레시피 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });
    }

}
