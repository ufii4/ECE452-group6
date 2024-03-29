package com.ece452.watfit.ui.post;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.ece452.watfit.R;
import com.ece452.watfit.data.models.DietaryLogPost;
import com.ece452.watfit.data.models.Post;
import com.ece452.watfit.ui.post.DietaryLogPostFragment;
import com.ece452.watfit.ui.post.PostFragment;
import com.google.firebase.storage.FirebaseStorage;

public class PostContentActivity extends AppCompatActivity {
    public static final String POST = "post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra(POST);

        FirebaseStorage.getInstance().getReference().child("posts/images/" + post.id + ".png").getDownloadUrl().addOnSuccessListener(uri -> {
            displayPost(post, uri);
        })
        .addOnFailureListener(e -> {
            displayPost(post, null);
        });
    }

    private void displayPost(Post post, Uri imageUri) {
        Fragment fragment;

        if (post.type == Post.PostType.DIETARY_LOG) {
            fragment = new DietaryLogPostFragment((DietaryLogPost) post);
        } else {
            fragment = new PostFragment(post, imageUri);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    // nav to post preview screen (activity_shared_with_me) if back button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}