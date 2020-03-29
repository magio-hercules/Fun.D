package com.fund.iam.ui.main.more.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fund.iam.R;
import com.fund.iam.data.model.Notice;
import com.orhanobut.logger.Logger;

import java.util.List;

public class NoticeAdapter extends BaseExpandableListAdapter {

    private List<Notice> notices;

    public NoticeAdapter(List<Notice> notices) {
        this.notices = notices;
    }

    public void addItems(List<Notice> notices) {
        this.notices.addAll(notices);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this.notices.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.notices.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.notices.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Notice notice = (Notice) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_notice, null);
        }
        TextView date = convertView.findViewById(R.id.date);
        date.setText(notice.getDate());
        TextView title = convertView.findViewById(R.id.title);
        title.setText(notice.getTitle());
        ImageView arrow = convertView.findViewById(R.id.arrow);
        arrow.setImageResource(isExpanded ? R.drawable.arrow_right : R.drawable.arrow_down);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Notice notice = (Notice) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_notice_detail, null);
        }
        TextView content = convertView.findViewById(R.id.content);
        content.setText(notice.getContent());
        Logger.d("content " + childPosition + ", " + notice.getContent());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}