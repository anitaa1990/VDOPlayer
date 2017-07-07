package com.an.videoplayer.model;

import com.an.videoplayer.utils.DataUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlaylistDb implements Serializable {

    private static final long serialVersionUID =5718948689677953814L;

    private static PlaylistDb playlistDb;
    public static PlaylistDb getInstance() {
        playlistDb = (PlaylistDb) DataUtils.readDbFromFile();
        if (playlistDb == null) {
            playlistDb = new PlaylistDb();
            DataUtils.writeDBToFile(playlistDb);
        }
        return playlistDb;
    }

    private Map<String, List<Video>> playlistMap = new HashMap<>();

    public void addToPlaylist(String playlist,
                              List<Video> videos) {
        List<Video> storedList = getVideosByPlaylist(playlist);
        if(storedList != null) {
            videos.addAll(storedList);
        }
        playlistMap.put(playlist, videos);
        setPlaylistMap(playlistMap);
    }

    public List<Video> getVideosByPlaylist(String playlist) {
        return playlistMap.get(playlist);
    }

    public Map<String, List<Video>> getAllPlayLists() {
        return playlistMap;
    }


    public void deleteFromPlaylist(String playlist,
                                   List<String> videoIds) {
        List<Video> storedList = getVideosByPlaylist(playlist);
        if(storedList != null) {
            for(Iterator it=storedList.iterator(); it.hasNext();) {
                Video video = (Video) it.next();
                if(videoIds.contains(video.getColumnId())) {
                    it.remove();
                }
            }
        }
        if(!storedList.isEmpty()) {
            playlistMap.put(playlist, storedList);
            setPlaylistMap(playlistMap);
        } else deletePlaylist(playlist);
    }


    public void deletePlaylist(String playlist) {
        playlistMap.remove(playlist);
        setPlaylistMap(playlistMap);
    }

    private void setPlaylistMap(Map<String, List<Video>> playlistMap) {
        this.playlistMap = playlistMap;
        DataUtils.writeDBToFile(playlistDb);
    }
}
