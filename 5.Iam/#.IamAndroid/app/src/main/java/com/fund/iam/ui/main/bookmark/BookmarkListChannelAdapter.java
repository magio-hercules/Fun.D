package com.fund.iam.ui.main.bookmark;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fund.iam.R;
import com.fund.iam.data.model.Channel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class BookmarkListChannelAdapter extends RecyclerView.Adapter<BookmarkListChannelAdapter.ViewHolder> {

    private List<Channel> channelsModel = new ArrayList<>();
    private List<Channel> channelsModel_filterKeyword = new ArrayList<>();
    String keyWord;

    @NonNull
    @Override
    public BookmarkListChannelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_channel_list, parent, false);

        return new BookmarkListChannelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkListChannelAdapter.ViewHolder holder, int position) {
        Logger.d("Channel Adapter onBindViewHolder start");
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.search_food_sample)
                .centerCrop()
                .into(holder.imageView);

        holder.tv_name.setText(channelsModel.get(position).name);
        holder.tv_description.setText(channelsModel.get(position).description);
        holder.iv_lock.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return channelsModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final ImageView iv_lock;
        public final TextView tv_name;
        public final TextView tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            iv_lock = itemView.findViewById(R.id.iv_lock);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }

    public void setModel_Channels(List<Channel> channelsModel) {
        this.channelsModel = channelsModel;
        Logger.d("channelsModel size : "+channelsModel.size());
        notifyDataSetChanged();
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        channelsModel_filterKeyword.removeAll(channelsModel_filterKeyword);
        for (int i=0; i<channelsModel.size(); i++) {
            if(channelsModel.get(i).name.contains(keyWord.toUpperCase())) {
                channelsModel_filterKeyword.add(channelsModel.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
