package com.project.study.studyproject.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.project.study.studyproject.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private static final String LOG_TAG = "[MAP]";

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.map_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView idTextView = (TextView) convertView.findViewById(R.id.textView_id);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.textView_name);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.textView_date);
        Button btnDelete = (Button) convertView.findViewById(R.id.map_button_delete);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        idTextView.setText(listViewItem.getId());
        nameTextView.setText(listViewItem.getName());
        dateTextView.setText(listViewItem.getDate());
        btnDelete.setOnClickListener(listViewItem.getClickListener());

//        Button btnCall = (Button) convertView.findViewById(R.id.map_button_delete);
//        btnCall.setTag(position);
//        btnCall.setOnClickListener(mOnClickListener);

        convertView.setTag(position);
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String id, String name, String date, View.OnClickListener listener) {
        ListViewItem item = new ListViewItem();

        item.setId(id);
        item.setName(name);
        item.setDate(date);
        item.setClickListener(listener);

        listViewItemList.add(item);
        Log.d(LOG_TAG, "ListViewAdapater addItem success");

        notifyDataSetChanged();
    }

    public void deleteItem(String id) {
        int count = listViewItemList.size();
        for (int i = 0; i < count; i++) {
            ListViewItem item = listViewItemList.get(i);
            if (id.equals(item.getId())) {
                listViewItemList.remove(i);
                notifyDataSetChanged();

                Log.d(LOG_TAG, "ListViewAdapater deleteItem success");
                break;
            }
        }
    }

//    Button.OnClickListener mOnClickListener = new  Button.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int position = Integer.parseInt( (v.getTag().toString()) );
////            System.out.println("contactList.get(position).getPhoneNumber()  :   " + contactList.get(position).getPhoneNumber());
//            Log.d(LOG_TAG, "delete onItemClick: id(" + position + ")");
//
//        }
//    };


}
