package com.huji.pnina;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CardStackListener, View.OnClickListener {

    private static Map<Integer, String> categories;
    private List<Post> postsList;
    private RecyclerView cardStackView;
    private LinearLayout progressbarLayout;
    private DatabaseReference reference;
    private boolean isFilter;
    private Map<Integer, Boolean> filterCategories;

    static{
        categories = new HashMap<>();
        categories.put(0, "מדוייקים");
        categories.put(1, "טבע");
        categories.put(2, "רוח");
        categories.put(3, "חברה");
        categories.put(4, "רפואה");
    }

    private CardStackAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        cardStackView.setLayoutManager(llm);
        postsList = new ArrayList<>();
        filterCategories = new HashMap<>();
        CardStackAdapter adapter = new CardStackAdapter(postsList, this);
        cardStackView.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("posts").limitToFirst(50)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Post> newPosts = new ArrayList<>();
                        for(DataSnapshot childSnap : dataSnapshot.getChildren()){
                            newPosts.add(childSnap.getValue(Post.class));
                        }
                        if(newPosts.size() != postsList.size()){
                            postsList = newPosts;
                            Collections.reverse(postsList);
                            setupSwipeView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.w(getLocalClassName(), "Failed to read value.", error.toException());
                    }
                });




    }

    private void findViews() {
        cardStackView = findViewById(R.id.card_stack_view);
        progressbarLayout = findViewById(R.id.progressbar_layout);
        showProgressBar();
        ImageView createPostView = findViewById(R.id.btn_create_post);
        createPostView.setOnClickListener(this);
        ChipGroup chips = findViewById(R.id.my_chips);

        for (Map.Entry<Integer, String> cat : categories.entrySet()) {
            Chip chip = new Chip(this);
            chip.setId(cat.getKey());
            chip.setTag(cat.getKey());

            chip.setText(cat.getValue());
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int tag = (int) compoundButton.getTag();
                    filterCategories.put(tag, b);
                    filteredByCategory();
                }
            });
            chips.addView(chip);

        }

        chips.invalidate();
    }

    public void filteredByCategory(){
        List<Integer> filteredCats = new ArrayList<>();
        for(Map.Entry<Integer, Boolean> pairs : filterCategories.entrySet()){
            if(pairs.getValue()){
                filteredCats.add(pairs.getKey());
            }
        }
        List<Post> postsInCategory = new ArrayList<>();
        for (int post=0; post<postsList.size(); post++){
            List<Integer> existCategories = postsList.get(post).getCategories();
            for (int category=0; category<filteredCats.size(); category++){
                if (existCategories.contains(filteredCats.get(category))){
                    postsInCategory.add(postsList.get(post));
                    break;
                }
            }

        }
        if(postsInCategory.size()>0){
            adapter.setFilteredData(postsInCategory);
        }else if (filteredCats.size() > 0){
            adapter.removeFilter();
            Toast.makeText(this, "אין מספיק פוסטים כאן למובז כאלה", Toast.LENGTH_SHORT).show();
        }

    }

    private void showProgressBar() {
        cardStackView.setVisibility(View.GONE);
        progressbarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressbarLayout.setVisibility(View.GONE);
        cardStackView.setVisibility(View.VISIBLE);
    }


    private void setupSwipeView(){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        cardStackView.setLayoutManager(llm);
        adapter = new CardStackAdapter(postsList, this);
        cardStackView.setAdapter(adapter);
        hideProgressBar();
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if(direction == Direction.Left){
            Toast.makeText(MainActivity.this, "totheLeft", Toast.LENGTH_SHORT).show();
        }else if(direction == Direction.Right){
            Toast.makeText(MainActivity.this, "totheRight", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }


    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        if (viewID == R.id.btn_create_post) {
            startCreatePostActivity();
        }
    }

    private void startCreatePostActivity(){
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }
}



