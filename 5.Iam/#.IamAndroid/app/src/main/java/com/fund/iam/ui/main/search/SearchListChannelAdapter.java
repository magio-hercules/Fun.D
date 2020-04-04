package com.fund.iam.ui.main.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fund.iam.R;
import com.fund.iam.data.model.Channel;
import com.fund.iam.data.model.User;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchListChannelAdapter extends RecyclerView.Adapter<SearchListChannelAdapter.ViewHolder>{

    private List<Channel> channelsModel = new ArrayList<>();


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

        holder.tv_name.setText(channelsModel.get(position).name);
        holder.tv_description.setText(channelsModel.get(position).description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(),position+"",Toast.LENGTH_SHORT).show();
                Logger.i("클릭"+position);
                //TODO Activity Fragment 선택
//                Intent intent = new Intent(channelsViewHolder.itemView.getContext(),ChanelMainActivity.class);
//                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return channelsModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView tv_name;
        public final TextView tv_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_description);

        }
    }


    public void setModel_Channels(List<Channel> channelsModel) {
        this.channelsModel = channelsModel;
        notifyDataSetChanged();
    }


}
