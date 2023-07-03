package com.ece452.watfit;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece452.watfit.data.Post;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostInboxAdapter extends RecyclerView.Adapter<PostInboxAdapter.InboxViewHolder> {
    private List<String> postIds;

    public PostInboxAdapter(List<String> postIds) {
        this.postIds = postIds;
    }

    @NonNull
    @Override
    public PostInboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shared_post, parent, false);
        return new PostInboxAdapter.InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostInboxAdapter.InboxViewHolder holder, int position) {
        String postId = postIds.get(position);
        FirebaseFirestore.getInstance().collection("posts").document(postId).get().addOnSuccessListener(documentSnapshot -> {
            Post post = documentSnapshot.toObject(Post.class);
            holder.title.setText(post.title);
            holder.description.setText(post.description);
            holder.author.setText(post.author);
        });
    }

    @Override
    public int getItemCount() {
        return postIds.size();
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView author;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(v.getContext(), PostContentActivity.class);
                    intent.putExtra("postId", postIds.get(position));
                    startActivity(v.getContext(), intent, null);
                }
            });
            title = itemView.findViewById(R.id.lb_title_csp);
            description = itemView.findViewById(R.id.lb_description_csp);
            author = itemView.findViewById(R.id.lb_sender_csp);
        }
    }
}