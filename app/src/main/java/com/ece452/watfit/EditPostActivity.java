package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ece452.watfit.data.Post;
import com.ece452.watfit.data.PostInbox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditPostActivity extends AppCompatActivity {
    private List<String> emails = new ArrayList<>();

    private EditText et_email;

    private EditText et_title;

    private EditText et_content;

    private Button bt_submit;

    @Inject
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        // set back button in top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        et_email = findViewById(R.id.et_email_ep);
        et_title = findViewById(R.id.et_title_ep);
        et_content = findViewById(R.id.et_content_ep);
        bt_submit = findViewById(R.id.bt_share_ep);

        bt_submit.setOnClickListener(view -> {
            String email = et_email.getText().toString();
            String title = et_title.getText().toString();
            String content = et_content.getText().toString();
            db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(this, "Could not find user with email: " + email, Toast.LENGTH_SHORT).show();
                    return;
                }
                String recipientId = queryDocumentSnapshots.getDocuments().get(0).getId();
                Post post = new Post();
                post.author = FirebaseAuth.getInstance().getUid();
                post.title = title;
                post.description = content;
                db.collection("posts").add(post).addOnSuccessListener(documentReference -> {
                    String postId = documentReference.getId();
                    PostInbox data = new PostInbox();
                    data.postId = postId;
                    data.authorId = post.author;
                    db.collection("users").document(recipientId).collection("inbox").add(data);
                    Toast.makeText(this, "Post shared with " + email, Toast.LENGTH_SHORT).show();
                });
            });
        });
    }

    // nav to previous screen when back button is clicked
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