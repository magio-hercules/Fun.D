package com.fund.iam.ui.main.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fund.iam.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public static final String TAG = SpinnerAdapter.class.getSimpleName();

    public SpinnerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        if (position == getCount()) {
//            Log.d(TAG, "position == getCount()");
            ((TextView)v.findViewById(R.id.spinner_item_text)).setText(getItem(getCount()));
//            ((TextView)v.findViewById(R.id.spinner_item_text)).setHint(getItem(getCount()));
            ((TextView)v.findViewById(R.id.spinner_item_text)).setTextColor(0xFFC9C9C9);
        } else {
        }

        ((TextView)v.findViewById(R.id.spinner_item_text)).setPadding(0, 10, 0, 10);
        return v;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count-1 : count ;
    }
}
