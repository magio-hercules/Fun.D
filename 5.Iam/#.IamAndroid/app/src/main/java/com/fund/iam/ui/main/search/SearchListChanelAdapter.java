package com.fund.iam.ui.main.search;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fund.iam.R;
import com.kakao.util.helper.log.Logger;

public class SearchListChanelAdapter extends RecyclerView.Adapter<SearchListChanelAdapter.ViewHoder>{
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chanel_list,parent,false);

        return new ViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoder holder, final int position) {

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.search_food_sample)
                .centerCrop()
                .into(holder.imageView);

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
        return 10;
    }

    public static class ViewHoder extends RecyclerView.ViewHolder {

        public final ImageView imageView;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
