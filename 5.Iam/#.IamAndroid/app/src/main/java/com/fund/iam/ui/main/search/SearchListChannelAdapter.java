package com.fund.iam.ui.main.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fund.iam.R;
import com.fund.iam.data.model.Channel;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchListChannelAdapter extends RecyclerView.Adapter<SearchListChannelAdapter.ViewHolder>{

    private List<Channel> channelsModel = new ArrayList<>();
    private List<Channel> channelsModel_filterKeyword = new ArrayList<>();
    String keyWord;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_list,parent,false);

        return new SearchListChannelAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.search_food_sample)
                .centerCrop()
                .into(holder.imageView);

        holder.tv_name.setText(channelsModel_filterKeyword.get(position).name);
        holder.tv_purpose.setText(channelsModel_filterKeyword.get(position).purpose);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("클릭"+position);

                int channelId = channelsModel_filterKeyword.get(position).id;
                SearchFragmentDirections.ActionMainChannel action = SearchFragmentDirections.actionMainChannel();
                action.setChannelIdArg(channelId);
                Navigation.findNavController(v).navigate(action);

            }
        });

    }

    @Override
    public int getItemCount() {
        return channelsModel_filterKeyword.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView tv_name;
        public final TextView tv_purpose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_purpose = itemView.findViewById(R.id.tv_purpose);

        }
    }


    public void setModel_Channels(List<Channel> channelsModel) {
        this.channelsModel = channelsModel;
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
