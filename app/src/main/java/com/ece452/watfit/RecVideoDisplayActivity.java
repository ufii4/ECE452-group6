package com.ece452.watfit;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ece452.watfit.R;

public class RecVideoDisplayActivity extends AppCompatActivity {

    String videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_video_display);

        VideoView video = (VideoView) findViewById(R.id.rec_video);
        Uri uri1 = Uri.parse(videoUrl);
        video.setVideoURI(uri1);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        mediaController.setMediaPlayer(video);
        video.setMediaController(mediaController);
        video.start();
    }
}