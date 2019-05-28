package com.huji.pnina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 27-May-19
 * Created by Nitsa
 */
public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardStackAdapterViewHolder> {

    private Context context;
    private List<Post> posts;
    private List<Post> filteredPosts;
    private List<Integer> likedPostsPositions;

    public CardStackAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.filteredPosts = posts;
        this.context = context;
        this.likedPostsPositions = new ArrayList<>();
    }

    @NonNull
    @Override
    public CardStackAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posts, parent,
                false);
        return new CardStackAdapterViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return filteredPosts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CardStackAdapterViewHolder holder, final int position) {
        final Post post = filteredPosts.get(position);
        holder.likes.setText(String.valueOf(post.getLikes()));
        holder.date.setText(post.getCreationDate());
        holder.content.setText(post.getContentPost());
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!likedPostsPositions.contains(position)) {
                    Toast.makeText(context, "החכמתי!", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("posts").child(post.getId()).child("likes").setValue(post.getLikes() +1);
                    post.addLike();
                    holder.heart.setImageDrawable(ContextCompat.getDrawable(context,
                            R.drawable.baseline_favorite_24));
                    holder.likes.setText(String.valueOf(post.getLikes()));
                    likedPostsPositions.add(position);
                }
            }
        });
    }

    public void setFilteredData(List<Post> filtered) {
        this.filteredPosts = filtered;
    }

    public void removeFilter() {
        this.filteredPosts = posts;
    }


    class CardStackAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView likes;
        TextView content;
        TextView date;
        CardView mainLayout;
        ImageView heart;

            public CardStackAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                likes = itemView.findViewById(R.id.txt_likes);
                content = itemView.findViewById(R.id.txt_content);
                date = itemView.findViewById(R.id.txt_date);
                mainLayout = itemView.findViewById(R.id.card_main);
                heart = itemView.findViewById(R.id.btn_like_post);
            }


        }

}
