package com.ece452.watfit.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ece452.watfit.R;
import com.ece452.watfit.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    // Your Video URL
    String videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";
    String videoUrl2 = "https://www.youtube.com/watch?v=i9BupglHdtM";
    String videoUrl3 = "https://www.youtube.com/embed/zWh3CShX_do";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
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