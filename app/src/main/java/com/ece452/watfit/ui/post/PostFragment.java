package com.ece452.watfit.ui.post;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ece452.watfit.R;
import com.ece452.watfit.data.Post;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {
    protected Post post;
    protected Uri imageUri;

    protected TextView title;
    protected TextView description;
    protected ImageView image;

    public PostFragment(Post post) {
        super();
        this.post = post;
    }

    public PostFragment(Post post, Uri imageUri) {
        super();
        this.post = post;
        this.imageUri = imageUri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        title = view.findViewById(R.id.lb_title_pc);
        description = view.findViewById(R.id.lb_content_pc);
        image = view.findViewById(R.id.iv_post_image_pc);

        if (title != null) {
            title.setText(post.title);
        }

        if (description != null) {
            description.setText(post.description);
        }

        if (image != null && imageUri != null) {
            Glide.with(getContext()).load(imageUri).into(image);
            final List<Uri> images = new ArrayList<>();
            images.add(imageUri);
            image.setOnClickListener(iv -> {
                new StfalconImageViewer.Builder<>(getContext(), images, new ImageLoader<Uri>() {
                    @Override
                    public void loadImage(ImageView imageView, Uri image) {
                        Glide.with(getContext()).load(image).into(imageView);
                    }
                }).show();
            });
        }
        return view;
    }
}
