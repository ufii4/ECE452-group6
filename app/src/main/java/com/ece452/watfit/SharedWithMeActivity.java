package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ece452.watfit.data.PostInbox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SharedWithMeActivity extends AppCompatActivity {
    private List<String> posts = new ArrayList<>();
    private PostInboxAdapter adapter;
    private RecyclerView rv_inbox;

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

        // TODO: Dynamically display post preview cards if there is at least 1 privately shared post by friend
        adapter = new PostInboxAdapter(posts);
        rv_inbox = findViewById(R.id.rv_inbox);
        rv_inbox.setAdapter(adapter);
        rv_inbox.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("inbox").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        PostInbox post = queryDocumentSnapshots.getDocuments().get(i).toObject(PostInbox.class);
                        posts.add(post.postId);
                        adapter.notifyItemInserted(posts.size() - 1);
                    }
                });
    }

    // nav to AccountActivity when back button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            startActivity(new Intent(SharedWithMeActivity.this, AccountActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}