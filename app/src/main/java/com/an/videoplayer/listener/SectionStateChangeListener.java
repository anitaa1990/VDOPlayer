package com.an.videoplayer.listener;

import com.an.videoplayer.model.Section;

public interface SectionStateChangeListener {
    void onSectionStateChanged(Section section, boolean isOpen);
}
