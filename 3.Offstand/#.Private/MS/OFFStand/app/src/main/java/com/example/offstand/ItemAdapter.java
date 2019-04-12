package com.example.offstand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

 public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<DBInfo> mDBInfos = Collections.emptyList(); // Cached copy of words

    public ItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = mInflater.inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DBInfo current = mDBInfos.get(i);

        viewHolder.rank.setImageResource(current.getRank());
        viewHolder.userImage.setImageResource(current.getUserImage());
        viewHolder.userId.setText(current.getUserId());
        viewHolder.win.setText(current.getWin()); // 이부분 수정
        viewHolder.lose.setText(current.getLose()); // 이부분 수정
    }

    @Override
    public int getItemCount() {
        return mDBInfos.size();
    }

    void setDBInfos(List<DBInfo> dbInfos) {
        mDBInfos = dbInfos;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView wordItemView;
        ImageView rank;
        ImageView userImage;
        TextView userId;
        TextView win;
        TextView lose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            userImage = itemView.findViewById(R.id.userImage);
            userId = itemView.findViewById(R.id.userId);
            win = itemView.findViewById(R.id.win);
            lose = itemView.findViewById(R.id.lose);
        }
    }
}