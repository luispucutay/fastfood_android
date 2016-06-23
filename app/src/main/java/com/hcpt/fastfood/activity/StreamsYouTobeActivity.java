package com.hcpt.fastfood.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.adapter.StreamsAdapter;
import com.hcpt.fastfood.object.Stream;

import java.util.ArrayList;

public class StreamsYouTobeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener,
        YouTubePlayer.PlayerStateChangeListener {

    public static final String API_KEY = "AIzaSyB8-72wYmZUsMsjLiC30yzNrZpl_hTkAPQ";
    private YouTubePlayerView playerView;
    private YouTubePlayer mPlayer;
    private boolean mAutoRotation = false;

    private Button btnTopBack;
    private TextView lblHeader;
    private RelativeLayout layoutHeader;
    private ListView lvStreams;

    private ArrayList<Stream> mArrStreams;
    private StreamsAdapter adapter;
    private Stream currentStream;


    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streams_youtobe);
        getDataIntent();
        initUI();
        initOrient();
        initData();
        initControl();
    }

    private void initUI() {
        playerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);
        playerView.initialize(API_KEY, this);
        layoutHeader = (RelativeLayout) findViewById(R.id.layout);
        lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
        lblHeader.setSelected(true);
        btnTopBack = (Button) findViewById(R.id.btnLeft);
        btnTopBack.setBackgroundResource(R.drawable.btn_back);
        lvStreams = (ListView) findViewById(R.id.lvStreams);
    }

    private void getDataIntent() {
        Bundle receiverBundle = this.getIntent().getExtras();
        mArrStreams= new ArrayList<>();
        currentStream = new Stream();
        mArrStreams = (ArrayList<Stream>) receiverBundle.get("listStreams");
        currentStream = (Stream) receiverBundle.get("streams");
        Log.e("Size list streams:", mArrStreams.size() + "");
        Log.e("Id current stream:", currentStream.getId());
    }

    private void initData() {
        adapter = new StreamsAdapter(this, mArrStreams);
        lvStreams.setAdapter(adapter);
        lblHeader.setText(currentStream.getTitle());
    }

    private void initControl() {
        btnTopBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lvStreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentStream = mArrStreams.get(position);
                lblHeader.setText(currentStream.getTitle());
                mPlayer.loadVideo(currentStream.getId());
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mPlayer != null)
                mPlayer.setFullscreen(true);
            hideSystemUI();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mPlayer != null)
                mPlayer.setFullscreen(false);
            showSystemUI();
        }
    }

    @Override
    public void onFullscreen(boolean fullsize) {
        if (fullsize) {
            setRequestedOrientation(LANDSCAPE_ORIENTATION);
        } else {
            setRequestedOrientation(PORTRAIT_ORIENTATION);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        mPlayer = player;
        player.setPlayerStateChangeListener(this);
        player.setOnFullscreenListener(this);

        if (mAutoRotation) {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        } else {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }

        if (!wasRestored) {
            player.loadVideo(currentStream.getId());
        }

    }

    private void initOrient() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutHeader.setVisibility(View.GONE);
        } else {
            layoutHeader.setVisibility(View.VISIBLE);
        }
        mAutoRotation = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {
        mPlayer.play();
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    private void hideSystemUI() {
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
