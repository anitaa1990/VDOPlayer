package com.an.videoplayer.utils;


import com.an.videoplayer.model.PlaylistDb;
import com.an.videoplayer.model.Video;

import java.util.List;
import java.util.Map;

public class DataUtils {

    private final static String APP_CONTEXT_FILENAME = "/data/data/com.an.videoplayer/DbContext.dat";

    public static void writeDBToFile(Object object) {
        writeObjectToDisk(APP_CONTEXT_FILENAME, object);
    }

    public static Object readDbFromFile() {
        return readObjectFromDisk(APP_CONTEXT_FILENAME);
    }

    public static void writeObjectToDisk(String fileName, Object object) {
        ObjectUtils objDataStream = new ObjectUtils();
        objDataStream.writeObject(object, fileName);
    }

    public static Object readObjectFromDisk(String fileName) {
        ObjectUtils objDataStream = new ObjectUtils();
        return objDataStream.readObject(fileName);
    }


    private PlaylistDb playlistDb;
    public DataUtils() {
        playlistDb = PlaylistDb.getInstance();
    }

    public void addPlaylist(String playlist,
                            List<Video> videoList) {
        playlistDb.addToPlaylist(playlist, videoList);
    }

    public void deletePlaylist(String playlist) {
        playlistDb.deletePlaylist(playlist);
    }

    public void deleteVideosFromPlaylist(String playlist,
                                         List<String> videoIds) {
        playlistDb.deleteFromPlaylist(playlist, videoIds);
    }

    public List<Video> getVideosByPlaylist(String playlist) {
        return playlistDb.getVideosByPlaylist(playlist);
    }

    public Map<String, List<Video>> getAllPlaylists() {
        return playlistDb.getAllPlayLists();
    }
}
