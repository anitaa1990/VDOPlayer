package com.an.videoplayer.views;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.an.videoplayer.adapter.VideoListAdapter;
import com.an.videoplayer.listener.ItemClickListener;
import com.an.videoplayer.listener.SectionStateChangeListener;
import com.an.videoplayer.model.Section;
import com.an.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpandableLayoutHelper implements SectionStateChangeListener {

    private LinkedHashMap<Section, List<Video>> mSectionDataMap = new LinkedHashMap<Section, List<Video>>();
    private ArrayList<Object> mDataArrayList = new ArrayList<Object>();

    private HashMap<String, Section> mSectionMap = new HashMap<String, Section>();

    private VideoListAdapter mSectionedExpandableGridAdapter;
    private RecyclerView mRecyclerView;

    private Context context;

    public ExpandableLayoutHelper(Context context, RecyclerView recyclerView,
                                  ItemClickListener itemClickListener,
                                  int gridSpanCount) {
        this.context = context;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedExpandableGridAdapter = new VideoListAdapter(context, mDataArrayList, gridLayoutManager, itemClickListener, this);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);

        mRecyclerView = recyclerView;
    }

    public void notifyDataSetChanged() {
        generateDataList();
        mSectionedExpandableGridAdapter.notifyDataSetChanged();
    }

    public void deleteSelectedItems() {
        mSectionedExpandableGridAdapter.deleteSelectedItems();
    }

    public int getSelectedItemCount() {
        return mSectionedExpandableGridAdapter.getSelectedItemCount();
    }

    public List<Video> getSelectedItems() {
        return mSectionedExpandableGridAdapter.getSelectedVideos();
    }

    public List<Video> getVideosFromSection(Section section) {
        return mSectionDataMap.get(mSectionMap.get(section.getName()));
    }

    public void clearSelectedItems() {
        mSectionedExpandableGridAdapter.clearSelections();
    }

    public String getSelectedPlaylist() {
        return mSectionedExpandableGridAdapter.getSelectedPlaylist();
    }

    public void addSection(String section, int imgRes, List<Video> videos) {
        Section newSection = new Section(section, imgRes);
        mSectionMap.put(section, newSection);
        mSectionDataMap.put(newSection, videos);
    }

    public void addSection(String section, int imgRes, List<Video> videos, boolean isExpanded) {
        Section newSection = new Section(section, imgRes, isExpanded);
        mSectionMap.put(section, newSection);
        mSectionDataMap.put(newSection, videos);
    }

    public void addItem(String section, Video video) {
        mSectionDataMap.get(mSectionMap.get(section)).add(video);
    }

    public void removeItem(String section, Video video) {
        mSectionDataMap.get(mSectionMap.get(section)).remove(video);
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    private void generateDataList () {
        mDataArrayList.clear();
        for (Map.Entry<Section, List<Video>> entry : mSectionDataMap.entrySet()) {
            Section key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                mDataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        if (!mRecyclerView.isComputingLayout()) {
            section.isExpanded = isOpen;
            notifyDataSetChanged();
        }
    }
}


