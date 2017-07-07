package com.an.videoplayer.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;


public class CreateThumbnail extends AsyncTask<Void, Void, Bitmap> {

    private Context context;
    private ImageView imageView;
    private String thumbNail;

    public CreateThumbnail(Context context, ImageView imageView, String thumbNail) {
        this.context = context;
        this.imageView = imageView;
        this.thumbNail = thumbNail;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(thumbNail, MediaStore.Video.Thumbnails.MINI_KIND);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Bitmap image = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        imageView.setImageBitmap(image);
    }
}
