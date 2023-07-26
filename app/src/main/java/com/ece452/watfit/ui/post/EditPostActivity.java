package com.ece452.watfit.ui.post;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ece452.watfit.R;
import com.ece452.watfit.data.models.Post;
import com.ece452.watfit.data.models.PostInbox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

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

    private ImageView iv_post_image;

    private Button bt_submit;

    public static final String SCREEN_SHOT = "screen_shot";

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

        byte[] image = getIntent().getByteArrayExtra(SCREEN_SHOT);
        Bitmap screenShot = null;
        if (image != null) {
            screenShot = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        et_email = findViewById(R.id.et_email_ep);
        et_title = findViewById(R.id.et_title_ep);
        et_content = findViewById(R.id.et_content_ep);
        bt_submit = findViewById(R.id.bt_share_ep);
        iv_post_image = findViewById(R.id.iv_post_image_ep);

        if (screenShot != null) {
            iv_post_image.setImageBitmap(screenShot);
            final List<Bitmap> images = new ArrayList<>();
            images.add(screenShot);
            iv_post_image.setOnClickListener(view -> {
                new StfalconImageViewer.Builder<>(this, images, new ImageLoader<Bitmap>() {
                    @Override
                    public void loadImage(ImageView imageView, Bitmap image) {
                        imageView.setImageBitmap(image);
                    }
                }).show();
            });
        }

        bt_submit.setOnClickListener(view -> {
            String email = et_email.getText().toString();
            String title = et_title.getText().toString();
            String content = et_content.getText().toString();
            if (email.isEmpty()) {
                savePost(title, content, image, null, null);
            } else {
                db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Could not find user with email: " + email, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String recipientId = queryDocumentSnapshots.getDocuments().get(0).getId();
                    savePost(title, content, image, email, recipientId);
                });
            }

        });
    }

    public Post getPost(String authorId, String title, String description, Bitmap screenShot) {
        Post post = new Post();
        post.author = authorId;
        post.title = title;
        post.description = description;
        return post;
    }

    protected void savePost(String title, String content, byte[] image, String email, String recipientId) {
        Bitmap screenShot = null;
        if (image != null) {
            screenShot = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        Post post = getPost(FirebaseAuth.getInstance().getUid(), title, content, screenShot);
        if (recipientId != null) {
            post.isPublic = false;
        } else {
            post.isPublic = true;
        }
        db.collection("posts").add(post).addOnSuccessListener(documentReference -> {
            String postId = documentReference.getId();
            PostInbox data = new PostInbox();
            data.postId = postId;
            data.authorId = post.author;
            if (recipientId != null) {
                db.collection("users").document(recipientId).collection("inbox").add(data);
            }
            StringBuilder message = new StringBuilder("Post shared");
            if (email != null) {
                message.append(" with ").append(email);
            }
            if (image != null) {
                StorageReference postImageRef = FirebaseStorage.getInstance().getReference("posts/images/" + postId + ".png");
                postImageRef.putBytes(image).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, post.toString(), Toast.LENGTH_SHORT).show();
            }
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