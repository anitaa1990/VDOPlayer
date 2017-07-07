package com.an.videoplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.an.videoplayer.R;
import com.an.videoplayer.listener.ItemClickListener;
import com.an.videoplayer.listener.SectionStateChangeListener;
import com.an.videoplayer.model.Section;
import com.an.videoplayer.model.Video;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.CustomViewHolder> {

    private static final int VIEW_TYPE_SECTION = R.layout.list_section;
    private static final int VIEW_TYPE_ITEM = R.layout.list_item;

    private SparseBooleanArray selectedItems;

    private Context context;
    private List<Object> videoList;
    private final ItemClickListener itemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    public VideoListAdapter(Context context,
                            List<Object> videoList,
                            final GridLayoutManager gridLayoutManager,
                            ItemClickListener itemClickListener,
                            SectionStateChangeListener mSectionStateChangeListener) {
        this.selectedItems = new SparseBooleanArray();
        this.context = context;
        this.videoList = videoList;
        this.itemClickListener = itemClickListener;
        this.mSectionStateChangeListener = mSectionStateChangeListener;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position)? gridLayoutManager.getSpanCount():1;
            }
        });
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        switch (holder.viewType) {

            case VIEW_TYPE_ITEM :
                final Video video = (Video) videoList.get(position);

                if(isSelected(position)) {
                    holder.selectorContainer.setBackgroundResource(R.drawable.list_selected);
                    holder.selectorContainer.setPadding(new Float(context.getResources().getDimension(R.dimen.padding_small)).intValue(),
                            new Float(context.getResources().getDimension(R.dimen.padding_small)).intValue(),
                            new Float(context.getResources().getDimension(R.dimen.padding_small)).intValue(),
                            new Float(context.getResources().getDimension(R.dimen.padding_small)).intValue());
                } else {
                    holder.selectorContainer.setBackground(null);
                    holder.selectorContainer.setPadding(0,0,0,0);
                }
                holder.durationTxt.setText(video.getDuration());
                Glide.with( context )
                        .load( Uri.fromFile( new File( video.getThumbNail() ) ) )
                        .override(280, 280)
                        .centerCrop()
                        .into( holder.imageview );
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getSelectedItemCount() > 0) {
                            toggleSelection(position);
                            itemClickListener.itemLongPressed(position, video);
                        }
                        else itemClickListener.itemClicked(position, video);
                    }
                });
                holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        toggleSelection(position);
                        itemClickListener.itemLongPressed(position, video);
                        return true;
                    }
                });
                break;

            case VIEW_TYPE_SECTION :
                final Section section = (Section) videoList.get(position);
                holder.sectionTextView.setText(section.getName());
                holder.sectionImageView.setImageResource(section.getImageRes());
                holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        toggleSelection(position);
                        itemClickListener.itemLongPressed(position, section);
                        return true;
                    }
                });

                if(section.isExpanded) holder.line.setVisibility(View.GONE);
                else holder.line.setVisibility(View.VISIBLE);

                holder.sectionToggleButton.setChecked(section.isExpanded);
                holder.sectionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(section.isExpanded) holder.line.setVisibility(View.GONE);
                        else holder.line.setVisibility(View.VISIBLE);
                        mSectionStateChangeListener.onSectionStateChanged(section, isChecked);
                    }
                });

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.itemClicked(position, section);
                        if(section.isExpanded) holder.line.setVisibility(View.GONE);
                        else holder.line.setVisibility(View.VISIBLE);
                        mSectionStateChangeListener.onSectionStateChanged(section, section.isExpanded ? false : true);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    private boolean isSection(int position) {
        return videoList.get(position) instanceof Section;
    }


    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void selectAllItems() {
        for(int pos =0; pos < videoList.size(); pos++) {
            if (!selectedItems.get(pos, false)) {
                selectedItems.put(pos, true);
            }
            notifyItemChanged(pos);
        }
    }

    public boolean isSelected(int position) {
        return selectedItems.get(position, false);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public List<Video> getSelectedVideos() {
        List<Video> selectedVideos = new ArrayList<>();
        for(int i=0; i< videoList.size(); i++) {
            if(videoList.get(i) instanceof Video) {
                Video video = (Video) videoList.get(i);
                if(selectedItems.get(i))
                    selectedVideos.add(video);
            }
        }
        return selectedVideos;
    }

    public String getSelectedPlaylist() {
        for(int i=0; i< videoList.size(); i++) {
            if(videoList.get(i) instanceof Section) {
                Section section = (Section) videoList.get(i);
                if(selectedItems.get(i))
                    return section.getName();
            }
        }
        return null;
    }

    public void deleteSelectedItem(int i) {
        videoList.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, videoList.size());
    }

    public void deleteSelectedItems() {
        List<Integer> selectedItemPositions = getSelectedItems();
        for (int i = (selectedItemPositions.size() - 1); i >= 0; i--) {
            deleteSelectedItem(selectedItemPositions.get(i));
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder  {
        private int viewType;
        private View view;

        private View selectorContainer;
        private TextView durationTxt;
        private ImageView imageview;

        private View line;
        private ImageView sectionImageView;
        private TextView sectionTextView;
        private ToggleButton sectionToggleButton;

        public CustomViewHolder(View view, int viewType) {
            super(view);
            this.view = view;
            this.viewType = viewType;

            if (viewType == VIEW_TYPE_ITEM) {
                this.selectorContainer = view.findViewById(R.id.selector_view);
                this.imageview = (ImageView) view.findViewById(R.id.image);
                this.durationTxt = (TextView) view.findViewById(R.id.duration_txt);
            } else {
                this.line = view.findViewById(R.id.section_line);
                this.sectionImageView = (ImageView) view.findViewById(R.id.img_icon);
                this.sectionTextView = (TextView) view.findViewById(R.id.text_section);
                this.sectionToggleButton = (ToggleButton) view.findViewById(R.id.toggle_button_section);
            }
        }
    }
}
