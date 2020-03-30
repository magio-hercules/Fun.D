package com.fund.iam.ui.main.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fund.iam.R;
import com.orhanobut.logger.Logger;

public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        if (position == getCount()) {
            Logger.i("position == getCount()");

//            ((TextView)v.findViewById(R.id.spinner_item_text)).setText("");
            ((TextView)v.findViewById(R.id.spinner_item_text)).setText(getItem(getCount()));
            ((TextView)v.findViewById(R.id.spinner_item_text)).setHint(getItem(getCount()));
        }
        return v;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count-1 : count ;
    }


}
