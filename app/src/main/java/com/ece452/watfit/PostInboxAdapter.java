package com.ece452.watfit;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece452.watfit.data.Post;
import com.ece452.watfit.data.UserProfile;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostInboxAdapter extends RecyclerView.Adapter<PostInboxAdapter.InboxViewHolder> {
    private List<Post> posts;

    public PostInboxAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostInboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shared_post, parent, false);
        return new PostInboxAdapter.InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostInboxAdapter.InboxViewHolder holder, int position) {
        Post post = posts.get(position);
        FirebaseFirestore.getInstance().collection("users").document(post.author).get().addOnSuccessListener(documentSnapshot1 -> {
            UserProfile author = documentSnapshot1.toObject(UserProfile.class);
            holder.title.setText(post.title);
            holder.description.setText(post.description);
            holder.author.setText(author.name);
            switch (post.type) {
                case MEAL_PLAN:
                    holder.type.setImageResource(R.drawable.baseline_dinner_dining_28);
                    break;
                case DIETARY_LOG:
                    holder.type.setImageResource(R.drawable.ic_fitness_tracker);
                    break;
                case FITNESS_DASHBOARD:
                    holder.type.setImageResource(R.drawable.ic_fitness_dashboard);
                    break;
                default:
                    holder.type.setImageResource(R.drawable.ic_about);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView author;
        public ImageView type;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(v.getContext(), PostContentActivity.class);
                    intent.putExtra(PostContentActivity.POST, posts.get(position));
                    startActivity(v.getContext(), intent, null);
                }
            });
            title = itemView.findViewById(R.id.lb_title_csp);
            description = itemView.findViewById(R.id.lb_description_csp);
            author = itemView.findViewById(R.id.lb_sender_csp);
            type = itemView.findViewById(R.id.iv_post_type_csp);
        }
    }
}
