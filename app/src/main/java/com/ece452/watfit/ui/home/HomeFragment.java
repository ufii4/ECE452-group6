package com.ece452.watfit.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ece452.watfit.MainActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.RecVideoDisplayActivity;
import com.ece452.watfit.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    // Your Video URL
    String videoCoverUrl1 = "https://img.youtube.com/vi/TSjXhAgQCis/0.jpg";
    String videoCoverUrl2 = "https://img.youtube.com/vi/zdpcw6CTkqw/0.jpg";
    String videoCoverUrl3 = "https://img.youtube.com/vi/iCQ2gC4DqJw/0.jpg";
    String videoCoverUrl4 = "https://img.youtube.com/vi/2ZbHFL_KhB4/0.jpg";
    String videoCoverUrl5 = "https://img.youtube.com/vi/ixkQaZXVQjs/0.jpg";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //may need to change starts
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //may need to change ends
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Code for image Covers start here
        ImageView v1 = (ImageView) root.findViewById(R.id.rec_v1);
        ImageView v2 = (ImageView) root.findViewById(R.id.rec_v2);
        ImageView v3 = (ImageView) root.findViewById(R.id.rec_v3);
        ImageView v4 = (ImageView) root.findViewById(R.id.rec_v4);
        ImageView v5 = (ImageView) root.findViewById(R.id.rec_v5);
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(videoCoverUrl1);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v1.setImageBitmap(bitmap);
        try {
            url = new URL(videoCoverUrl2);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v2.setImageBitmap(bitmap);
        try {
            url = new URL(videoCoverUrl3);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v3.setImageBitmap(bitmap);
        try {
            url = new URL(videoCoverUrl4);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v4.setImageBitmap(bitmap);
        try {
            url = new URL(videoCoverUrl5);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v5.setImageBitmap(bitmap);

        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                Intent intent = new Intent(getActivity(), RecVideoDisplayActivity.class);
                startActivity(intent);
            }
        });
        //final TextView textView = binding.textHome;
        //
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        /*// Video 1
        VideoView videoView1 = (VideoView) root.findViewById(R.id.rec_v1);
        Uri uri1 = Uri.parse(videoUrl);
        videoView1.setVideoURI(uri1);
        MediaController mediaController = new MediaController(root.getContext());
        mediaController.setAnchorView(videoView1);
        mediaController.setMediaPlayer(videoView1);
        videoView1.setMediaController(mediaController);
        //videoView1.start();
        //Video 2
        VideoView videoView2 = (VideoView) root.findViewById(R.id.rec_v2);
        Uri uri2 = Uri.parse(videoUrl);
        videoView2.setVideoURI(uri2);
        mediaController.setAnchorView(videoView2);
        mediaController.setMediaPlayer(videoView2);
        videoView2.setMediaController(mediaController);
        //videoView2.start();
        //Video 3
        VideoView videoView3 = (VideoView) root.findViewById(R.id.rec_v3);
        Uri uri3 = Uri.parse(videoUrl);
        videoView3.setVideoURI(uri3);
        mediaController.setAnchorView(videoView3);
        mediaController.setMediaPlayer(videoView3);
        videoView3.setMediaController(mediaController);
        //videoView3.start();
        //Video 4
        VideoView videoView4 = (VideoView) root.findViewById(R.id.rec_v4);
        Uri uri4 = Uri.parse(videoUrl);
        videoView4.setVideoURI(uri4);
        mediaController.setAnchorView(videoView4);
        mediaController.setMediaPlayer(videoView4);
        videoView4.setMediaController(mediaController);
        //videoView4.start();
        //Video 5
        VideoView videoView5 = (VideoView) root.findViewById(R.id.rec_v5);
        Uri uri5 = Uri.parse(videoUrl);
        videoView5.setVideoURI(uri5);
        mediaController.setAnchorView(videoView5);
        mediaController.setMediaPlayer(videoView5);
        videoView5.setMediaController(mediaController);
        //videoView5.start();
        //Videos end*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}