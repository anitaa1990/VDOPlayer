package com.an.videoplayer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.an.videoplayer.R;
import com.an.videoplayer.listener.ItemClickListener;
import com.an.videoplayer.listener.RefreshListener;
import com.an.videoplayer.model.Section;
import com.an.videoplayer.model.Video;
import com.an.videoplayer.utils.BaseUtils;
import com.an.videoplayer.utils.DataUtils;
import com.an.videoplayer.utils.DialogUtils;
import com.an.videoplayer.views.ExpandableLayoutHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends PermissionActivity implements ItemClickListener, View.OnClickListener, RefreshListener {

    private List<Video> videoList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ExpandableLayoutHelper sectionedExpandableLayoutHelper;
    private RefreshListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_main);
        initToolbar();

        listener = this;
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        if(permissionUtils.isPermissionGranted(PERMISSION_STORAGE)) init();
    }

    private void init() {
        videoList = BaseUtils.fetchAllVideosFromDevice(this);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        sectionedExpandableLayoutHelper = new ExpandableLayoutHelper(this, recyclerView, this, 3);

        Map<String, List<Video>> playlists = new DataUtils().getAllPlaylists();
        for(Map.Entry<String, List<Video>> mapEntry : playlists.entrySet()) {
            for(Video video : mapEntry.getValue()) {
                video.setSection(mapEntry.getKey());
            }
            sectionedExpandableLayoutHelper.addSection(mapEntry.getKey(), R.drawable.ic_playlist, mapEntry.getValue(), false);
        }
        sectionedExpandableLayoutHelper.addSection(getString(R.string.video_list_title), R.drawable.ic_videos, videoList, playlists.isEmpty());
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        askPermission();
    }

    @Override
    public void onPermissionGranted(String[] permissions, int[] grantResults) {
        init();
    }

    @Override
    public void onPermissionDismissed(String permission) {
        /**
         * You can close the application
         * since it would not work without
         * reading the external storage
         * * */
        finish();
    }

    @Override
    public void onPositiveButtonClicked(DialogInterface dialog, int which) {
        /**
         * You can choose to open the
         * app settings screen
         * * */
        permissionUtils.openAppSettings();
    }

    @Override
    public void onNegativeButtonClicked(DialogInterface dialog, int which) {
        /**
         * You can close the application
         * since it would not work without
         * reading the external storage
         * * */
        finish();
    }

    @Override
    public void itemClicked(int position, Video video) {
        BaseUtils.openVideoScreen(MainActivity.this, video, videoList, position);
    }

    @Override
    public void itemLongPressed(int position, Video video) {
        if(selectVideoBtn != null ) {
            selectVideoBtn.setOnClickListener(this);
            if(sectionedExpandableLayoutHelper.getSelectedItemCount() > 0) {
                selectVideoBtn.setVisibility(View.VISIBLE);
            } else selectVideoBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void itemLongPressed(int position, Section section) {
        List<Video> sectionVideos = sectionedExpandableLayoutHelper.getVideosFromSection(section);
        new DialogUtils().displaySelectionDialog(MainActivity.this, listener, section, sectionVideos);
    }

    @Override
    public void itemClicked(int position, Section section) {
        if(selectVideoBtn != null ) {
            sectionedExpandableLayoutHelper.clearSelectedItems();
            selectVideoBtn.setVisibility(View.INVISIBLE);
            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == selectVideoBtn) {
            new DialogUtils().displayOptionDialog(MainActivity.this, listener, sectionedExpandableLayoutHelper.getSelectedItems());
        }
    }

    @Override
    public void onRefresh(int action) {
        switch (action) {
            case 0:
                init();
                break;
            case 1:
                sectionedExpandableLayoutHelper.deleteSelectedItems();
                break;
        }
    }
}
