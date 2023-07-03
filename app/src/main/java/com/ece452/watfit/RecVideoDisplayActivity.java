package com.ece452.watfit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ece452.watfit.R;

public class RecVideoDisplayActivity extends AppCompatActivity {

    String videoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_video_display);

        // add back button to the top-left corner
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }

        /*VideoView video = (VideoView) findViewById(R.id.rec_video);
        Uri uri1 = Uri.parse(videoUrl);
        video.setVideoURI(uri1);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        mediaController.setMediaPlayer(video);
        video.setMediaController(mediaController);
        video.start();*/
        Bundle para = getIntent().getExtras();
        if(para != null)
            videoUrl = para.getString("VideoURL");

        WebView wv = (WebView) findViewById(R.id.rec_video);
        WebSettings websettings = wv.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setDomStorageEnabled(true);
        websettings.setDatabaseEnabled(true);
        wv.loadUrl(videoUrl);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());
        wv.setPadding(0, 0, 0, 0);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
    }

    // nav to home screen if back button is clicked
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