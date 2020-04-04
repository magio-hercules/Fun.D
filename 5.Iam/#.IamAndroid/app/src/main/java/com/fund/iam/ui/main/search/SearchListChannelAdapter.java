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
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchListChannelAdapter extends RecyclerView.Adapter<SearchListChannelAdapter.ViewHoder>{

    private List<Channel> channelsModel = new ArrayList<>();

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_list,parent,false);

        return new ViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder holder, final int position) {

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.search_food_sample)
                .centerCrop()
                .into(holder.imageView);

        holder.iv_name.setText(channelsModel.get(position).name);
        holder.iv_description.setText(channelsModel.get(position).description);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(),position+"",Toast.LENGTH_SHORT).show();
                Logger.i("클릭"+position);
                //TODO Activity Fragment 선택
//                Intent intent = new Intent(holder.itemView.getContext(),ChanelMainActivity.class);
//                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return channelsModel.size();
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView iv_name;
        public final TextView iv_description;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            iv_name = itemView.findViewById(R.id.iv_name);
            iv_description = itemView.findViewById(R.id.iv_description);

        }
    }

    public void setModel_Channels(List<Channel> channelsModel) {
        this.channelsModel = channelsModel;
        notifyDataSetChanged();

    }
}
