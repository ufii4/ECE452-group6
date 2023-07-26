package com.ece452.watfit.ui.account;

import com.ece452.watfit.data.models.PostInbox;
import com.ece452.watfit.ui.post.PostListingActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SharedWithMeActivity extends PostListingActivity {
    @Override
    public Task<List<DocumentSnapshot>> getPosts() {
        String uid = FirebaseAuth.getInstance().getUid();
        TaskCompletionSource<List<DocumentSnapshot>> taskCompletionSource = new TaskCompletionSource<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).collection("inbox").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        PostInbox post = snapshot.toObject(PostInbox.class);
                        Task<DocumentSnapshot> postTask = db.collection("posts").document(post.postId).get();
                        tasks.add(postTask);
                    }

                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(r -> {
                        List<DocumentSnapshot> snapshots = new ArrayList<>();
                        for (Object o : r) {
                            snapshots.add((DocumentSnapshot) o);
                        }
                        taskCompletionSource.setResult(snapshots);
                    });
                });

        return taskCompletionSource.getTask();
    }
}
