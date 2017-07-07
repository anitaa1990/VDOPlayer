package com.an.videoplayer.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.an.videoplayer.R;
import com.an.videoplayer.adapter.DialogAdapter;
import com.an.videoplayer.listener.RefreshListener;
import com.an.videoplayer.model.Section;
import com.an.videoplayer.model.Video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DialogUtils {

    private DataUtils dataUtils;
    public DialogUtils() {
        this.dataUtils = new DataUtils();
    }

    public void displayOptionDialog(final Activity activity,
                                    final RefreshListener listener,
                                    final List<Video> videos) {
        final String playlist = videos.get(0).getSection();
        final List<String> list = new LinkedList<String>(Arrays.asList(activity.getResources().getStringArray(R.array.option_items)));
        if(playlist == null) {
            list.remove(list.size()-1);
        }
        final String[] items = list.toArray(new String[0]);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
                        .setTitle(activity.getString(R.string.dialog_option_title))
                        .setCancelable(true)
                        .setItems(items,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(items[which].equalsIgnoreCase(activity.getString(R.string.dialog_option_play))) {
                                            //play
                                            BaseUtils.openVideoScreen(activity, videos.get(0), videos, 0);

                                        } else if(items[which].equalsIgnoreCase(activity.getString(R.string.dialog_option_delete))) {
                                            //delete from playlist - dialog to confirm if users wnats to delete
                                            displayDeleteVideoDialog(activity, listener, playlist, videos);

                                        } else {
                                            //add to playlist - new dialog displaying the existing playlists
                                            if(dataUtils.getAllPlaylists().size() == 0) createPlaylistDialog(activity, listener, videos);
                                            else {
                                                //display list of playlists for users to choose
                                                selectPlaylistDialog(activity, listener, videos);
                                            }
                                        }

                                    }
                                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void displaySelectionDialog(final Activity activity,
                                       final RefreshListener listener,
                                       final Section section,
                                       final List<Video> videos) {

        final String playlist = section.getName();
        final List<String> list = new LinkedList<String>(Arrays.asList(activity.getResources().getStringArray(R.array.option_items)));
        list.remove(0);

        final String[] items = list.toArray(new String[0]);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
                .setTitle(activity.getString(R.string.dialog_option_title))
                .setCancelable(true)
                .setItems(items,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(items[which].equalsIgnoreCase(activity.getString(R.string.dialog_option_play))) {
                                    //play
                                    BaseUtils.openVideoScreen(activity, videos.get(0), videos, 0);

                                } else if(items[which].equalsIgnoreCase(activity.getString(R.string.dialog_option_delete))) {
                                    //delete from playlist - dialog to confirm if users wnats to delete
                                    displayDeleteVideoDialog(activity, listener, playlist, videos);

                                } else {
                                    //add to playlist - new dialog displaying the existing playlists
                                    if(dataUtils.getAllPlaylists().size() == 0) createPlaylistDialog(activity, listener, videos);
                                    else {
                                        //display list of playlists for users to choose
                                        selectPlaylistDialog(activity, listener, videos);
                                    }
                                }

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void displayDeleteVideoDialog(final Activity activity,
                                         final RefreshListener listener,
                                         final String playlist,
                                         final List<Video> videos) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
                .setTitle(String.format(activity.getString(R.string.delete_video_title), String.valueOf(videos.size())))
                .setMessage(activity.getString(R.string.delete_video_message))
                .setPositiveButton(activity.getString(R.string.delete_video_pos_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> videoIds = new ArrayList<String>();
                        for(Video video : videos) {
                            videoIds.add(video.getColumnId());
                        }
                        dataUtils.deleteVideosFromPlaylist(playlist, videoIds);
                        Toast.makeText(activity, activity.getString(R.string.delete_video_success), Toast.LENGTH_LONG).show();
                        listener.onRefresh(1);
                    }
                })
                .setNegativeButton(activity.getString(R.string.delete_video_neg_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void createPlaylistDialog(final Activity activity,
                                     final RefreshListener listener,
                                     final List<Video> videos) {
        LayoutInflater li = LayoutInflater.from(activity);
        View view = li.inflate(R.layout.dialog_create_playlist, null);
        final EditText editText = (EditText) view.findViewById(R.id.playlist_name);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
                .setTitle(activity.getString(R.string.create_playlist_title))
                .setView(view)
                .setPositiveButton(activity.getString(R.string.create_playlist_pos_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editText.getText().toString().length() == 0) {
                            Toast.makeText(activity, activity.getString(R.string.create_playlist_error), Toast.LENGTH_LONG).show();
                        } else {
                            dataUtils.addPlaylist(editText.getText().toString(), videos);
                            Toast.makeText(activity, activity.getString(R.string.create_playlist_success), Toast.LENGTH_LONG).show();
                            listener.onRefresh(0);
                        }
                    }
                })
                .setNegativeButton(activity.getString(R.string.create_playlist_neg_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void selectPlaylistDialog(final Activity activity,
                                     final RefreshListener listener,
                                     final List<Video> videos) {
        AlertDialog alertDialog = null;

        LayoutInflater li = LayoutInflater.from(activity);
        View dialogView = li.inflate(R.layout.dialog_select_playlist, null);

        final ListView listView = (ListView) dialogView.findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        final TextView addTextBtn = (TextView) dialogView.findViewById(R.id.playlist_name);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle)
                .setTitle(activity.getString(R.string.select_playlist_title))
                .setView(dialogView)
                .setCancelable(true);
        alertDialog = alertDialogBuilder.create();

        Map<String, List<Video>> playlists = dataUtils.getAllPlaylists();
        Set<String> playlistNames = playlists.keySet();
        DialogAdapter adapter = new DialogAdapter(playlistNames, videos, activity, alertDialog, listener);
        listView.setAdapter(adapter);

        final AlertDialog finalAlertDialog = alertDialog;
        addTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlaylistDialog(activity, listener, videos);
                finalAlertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
