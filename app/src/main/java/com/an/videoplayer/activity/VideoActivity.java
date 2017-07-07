package com.an.videoplayer.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;
import com.an.videoplayer.R;
import com.an.videoplayer.model.Video;
import com.an.videoplayer.utils.BaseUtils;

import java.util.List;


public class VideoActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener, MediaPlayer.OnCompletionListener {


    private Video video;
    private int currentVideoPos = 0;
    private List<Video> videos;

    private VideoView videoView;
    private View frameView;
    private View volumeView;

    private ImageButton mCloseButton;
    private SeekBar mVolumeProgress;

    private int position = 0;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;

    private AudioManager audioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        video = (Video) getIntent().getSerializableExtra("video");
        currentVideoPos = getIntent().getIntExtra("position", 0);
        videos = (List<Video>) getIntent().getSerializableExtra("videoList");

        videoView = (VideoView) findViewById(R.id.video_view);
        frameView = findViewById(R.id.video_container);
        frameView.setOnTouchListener(this);
        videoView.setOnTouchListener(this);
        volumeView = findViewById(R.id.volume_view);

        mediaController  = new MediaController(this, true);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousVideo();
            }
        });
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setVideoPath(video.getColumn());
        disableFullScreen();
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mediaPlayer) {
                videoView.seekTo(position);
                if(position == 0) videoView.start();
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        VideoActivity.this.mediaPlayer = mediaPlayer;
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

        initVideoControls();
    }

    private void initVideoControls() {
        if(audioManager == null) audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mCloseButton = (ImageButton) findViewById(R.id.close_btn);
        mCloseButton.setOnClickListener(this);

        mVolumeProgress = (SeekBar) findViewById(R.id.mediacontroller_volume);
        mVolumeProgress.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mVolumeProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }



    private void disableFullScreen() {
        int width = BaseUtils.getScreenWidth(this);
        int height = BaseUtils.getScreenHeight(this);
        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) frameView.getLayoutParams();
        params.width =  width;
        params.height = height;
        params.leftMargin = 1;
        frameView.setLayoutParams(params);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == mCloseButton) {
            if(mediaPlayer != null) {
                mediaPlayer.stop();
                finish();
            }
        } else {
            if (volumeView.getVisibility() == View.VISIBLE) volumeView.setVisibility(View.INVISIBLE);
            else if (volumeView.getVisibility() == View.INVISIBLE) volumeView.setVisibility(View.VISIBLE);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == mCloseButton) {
            if(mediaPlayer != null) {
                mediaPlayer.stop();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNextVideo();
    }

    private void playPreviousVideo() {
        if(currentVideoPos ==0)
            currentVideoPos = 1;

        currentVideoPos = currentVideoPos-1;
        video = videos.get(currentVideoPos);
        position = 0;
        playVideo();
    }

    private void playNextVideo() {
        if(currentVideoPos == videos.size()-1)
            currentVideoPos = -1;

        currentVideoPos = currentVideoPos+1;
        video = videos.get(currentVideoPos);
        position = 0;
        playVideo();
    }

    private void playVideo() {
        videoView.setVideoPath(video.getColumn());
        videoView.seekTo(position);
        videoView.start();
    }
}
