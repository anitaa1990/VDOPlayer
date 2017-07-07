package com.an.videoplayer.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.an.videoplayer.R;
import com.an.videoplayer.listener.RefreshListener;
import com.an.videoplayer.model.Video;
import com.an.videoplayer.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DialogAdapter extends ArrayAdapter<String> {

    private List<String> dataSet;
    private List<Video> videos;
    private Context mContext;
    private AlertDialog alertDialog;
    private RefreshListener listener;

    private static class ViewHolder {
        private View container;
        TextView nameTxt;
        ImageView icon;
    }

    public DialogAdapter(Set<String> data,
                         List<Video> videos,
                         Context context,
                         AlertDialog alertDialog,
                         RefreshListener listener) {
        super(context, R.layout.list_section, new ArrayList(data));
        this.videos = videos;
        this.mContext = context;
        this.alertDialog = alertDialog;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String s = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_section, parent, false);
            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.text_section);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
            viewHolder.container = convertView.findViewById(R.id.list_item_container);

            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  playlist = getItem(position);
                    new DataUtils().addPlaylist(playlist, videos);
                    Toast.makeText(mContext, mContext.getString(R.string.select_playlist_success), Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    listener.onRefresh(0);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTxt.setText(s);
        viewHolder.icon.setImageResource(R.drawable.ic_playlist);

        return convertView;
    }
}
