package com.an.videoplayer.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.an.videoplayer.activity.VideoActivity;
import com.an.videoplayer.model.Video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BaseUtils {

    public static void openVideoScreen(Activity activity,
                                       Video video,
                                       List<Video> videos,
                                       int position) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("video", video);
        intent.putExtra("position", position);
        intent.putExtra("videoList", (Serializable) videos);
        activity.startActivity(intent);
    }

    public static List<Video> fetchAllVideosFromDevice(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.VideoColumns.DURATION};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
        int columnIndexFolderName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        int columnId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        int thumbNail = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        int durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION);

        List<Video> videos = new ArrayList<>();
        while (cursor.moveToNext()) {

            Video video = new Video();
            video.setColumn(cursor.getString(columnIndexData));
            video.setColumnId(cursor.getString(columnId));
            video.setFolderPath(cursor.getString(columnIndexFolderName));
            video.setThumbNail(cursor.getString(thumbNail));

            int duration = cursor.getInt(durationIndex);
            String d = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
            video.setDuration(d);
            videos.add(video);
        }
        return videos;
    }

    public static int getScreenWidth(Context mContext) {
        WindowManager wm = (WindowManager)mContext.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        int width1;
        if(Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width1 = size.x;
        } else {
            width1 = display.getWidth();
        }
        return width1;
    }

    public static int getScreenHeight(Context mContext) {
        WindowManager wm = (WindowManager)mContext.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        int height1;
        if(Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height1 = size.y;
        } else {
            height1 = display.getHeight();
        }
        return height1;
    }
}
