package com.an.videoplayer.listener;

import com.an.videoplayer.model.Section;
import com.an.videoplayer.model.Video;

public interface ItemClickListener {
    void itemClicked(int position, Video video);
    void itemLongPressed(int position, Video video);
    void itemLongPressed(int position, Section section);
    void itemClicked(int position, Section section);
}
