package com.an.videoplayer.model;

public class Section {

    private final String name;
    private final int imageRes;

    public boolean isExpanded;

    public Section(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
        isExpanded = false;
    }

    public Section(String name, int imageRes, boolean isExpanded) {
        this.name = name;
        this.imageRes = imageRes;
        this.isExpanded = isExpanded;
    }

    public String getName() {
        return name;
    }

    public int getImageRes() {
        return imageRes;
    }
}
