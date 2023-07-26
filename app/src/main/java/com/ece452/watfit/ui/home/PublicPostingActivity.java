package com.ece452.watfit.ui.home;

import com.ece452.watfit.ui.post.PostListingActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class PublicPostingActivity extends PostListingActivity {

    @Override
    protected Task<List<DocumentSnapshot>> getPosts() {
        return FirebaseFirestore.getInstance().collection("posts")
                .whereEqualTo("isPublic", true)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .continueWith(task -> task.getResult().getDocuments());
    }
}
