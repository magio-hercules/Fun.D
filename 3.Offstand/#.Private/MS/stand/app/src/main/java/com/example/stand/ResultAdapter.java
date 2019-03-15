package com.example.stand;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

class ResultAdapter extends BaseAdapter {

    ArrayList<ResultItem> items = new ArrayList<ResultItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override //ID 값 넘겨주기
    public long getItemId(int position) {
        return position;
    }

    public void addItem(ResultItem item) {
        items.add(item);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ResultItemView view = new ResultItemView(parent.getContext());
//                                                //getApplicationContext
//        ResultItem item = items.get(position);
//
//        view.setMyImage(item.getResId());
//        view.setID(item.getId());
//        view.setScore(item.getScore());
//
//        return view;
//    }

    @Override // 재사용 코드
    public View getView(int position, View convertView, ViewGroup parent) {
        ResultItemView view = null;
        if (convertView == null) {
            view = new ResultItemView(parent.getContext()); //getApplicationContext
        } else {
            view = (ResultItemView) convertView;
        }

        ResultItem item = items.get(position);

        view.setRank(item.getRank());
        view.setId(item.getUserId());
        view.setWin(item.getWin());
        view.setLose(item.getLose());

        Log.d("@@!","@@!");

        return view;
    }
}