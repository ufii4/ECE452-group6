package com.ece452.watfit.ui.post;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.ece452.watfit.R;
import com.ece452.watfit.data.models.Post;
import com.ece452.watfit.ui.account.PostInboxAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class PostListingActivity extends AppCompatActivity {
    private List<Post> posts = new ArrayList<>();
    private PostInboxAdapter adapter;
    private RecyclerView rv_inbox;

    protected abstract Task<List<DocumentSnapshot>> getPosts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_with_me);

        // set back button in top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        adapter = new PostInboxAdapter(posts);
        rv_inbox = findViewById(R.id.rv_inbox);
        rv_inbox.setAdapter(adapter);
        rv_inbox.setLayoutManager(new LinearLayoutManager(this));

        getPosts().addOnSuccessListener(documentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                String postId = documentSnapshot.getId();
                Post post = documentSnapshot.toObject(Post.class);
                post.id = postId;
                posts.add(post);
                adapter.notifyItemInserted(posts.size() - 1);
            }
        });
    }

    // nav to AccountActivity when back button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}