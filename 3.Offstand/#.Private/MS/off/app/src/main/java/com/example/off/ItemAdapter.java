package com.example.off;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//어뎁터 생성
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;

    ArrayList<Item> items = new ArrayList<Item>();

    OnItemClickListener listener;

    //listener에 데이터를 보관

    //생성자
    public ItemAdapter(Context context) {
        super();
        this.context = context;
    }

    //ViewHolder가 만들어지는 시점에 호출됨
    //재사용될 경우 호출되지 않음
    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    //데이터와 ViewHolder 어느 시점에 서로 뭉쳐짐
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder, int i) {
        viewHolder.rank.setImageResource(items.get(i).rank);
        viewHolder.userId.setText(items.get(i).userId);
        viewHolder.win.setText(items.get(i).win);
        viewHolder.lose.setText(items.get(i).lose);

        //리사이클 뷰에서는 리스트뷰처럼 제공해주는 클릭 리스너가 없어서 설정해줌
        viewHolder.setOnItemClickListener(listener);
    }

    //아이템의 갯수
    @Override
    public int getItemCount() {
        return 0;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    //여러개 한번에 추가 하기
    public void addItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Item getItem(int i) {
        return items.get(i);
    }

    //클릭 리스너 정의
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static interface OnItemClickListener {
        public void onItemClick(ViewHolder holder, View view, int i);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView rank;
        TextView userId;
        TextView win;
        TextView lose;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = (ImageView) itemView.findViewById(R.id.rank);
            userId = (TextView) userId.findViewById(R.id.userId);
            win = (TextView) win.findViewById(R.id.win);
            lose = (TextView) lose.findViewById(R.id.lose);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, v, i);
                    }
                }
            });
        }

        //데이터 설정
        public void setItem(Item item) {
            rank.setImageResource(item.getRank());
            userId.setText(item.getUserId());
            win.setText(item.getWin());
            lose.setText(item.getLose());
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}
