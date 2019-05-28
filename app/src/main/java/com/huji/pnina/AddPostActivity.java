package com.huji.pnina;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        if(this.getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        reference = FirebaseDatabase.getInstance().getReference();



        CardView postBtn = findViewById(R.id.card_confirm);
        postBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText input_post = findViewById(R.id.new_post_text);
                String postText = input_post.getText().toString();
                if (!postText.isEmpty()){
                    List<Integer> category = new ArrayList<>();
                    ChipGroup chipGroup = findViewById(R.id.chip_group);
                    int chips = chipGroup.getChildCount();
                    for (int i=0; i<chips; i++){
                        Chip chippie = (Chip) chipGroup.getChildAt(i);
                        if(chippie.isChecked()){
                            category.add(i);
                        }
                    }
                    if (category.size() > 0) {
                        Post post = new Post(category, postText);
                        DatabaseReference pushRef = reference.child("posts").push();
                        post.setId(pushRef.getKey());
                        pushRef.setValue(post);
                        Toast.makeText(AddPostActivity.this, "שוגַרררר אחותיייי",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddPostActivity.this, "תבחרי קטגוריה נשמה", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    input_post.setError("הלוווו תכתבי משהו");
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
