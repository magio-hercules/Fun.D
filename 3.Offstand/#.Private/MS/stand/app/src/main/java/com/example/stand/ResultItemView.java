package com.example.stand;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultItemView extends LinearLayout {

    ImageView rank;
    TextView userId;
    TextView win;
    TextView lose;

    public ResultItemView(Context context) {
        super(context);

        init(context);
    }

    public ResultItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.result_item, this, true);

        rank = (ImageView) findViewById(R.id.rank);
        userId = (TextView) findViewById(R.id.userId);
        win = (TextView) findViewById(R.id.win);
        lose = (TextView) findViewById(R.id.lose);


    }

    public void setRank(int _rank) {
        rank.setImageResource(_rank);
    }

    public void setId(String _id) {
        userId.setText(_id);
    }

    public void setWin(String _win) {
        win.setText(_win);
    }

    public void setLose(String _lose) {
        lose.setText(_lose);
    }
}
