package study.easycalendar.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import study.easycalendar.R;
import study.easycalendar.model.Dday;

public class DdayAdapter extends RecyclerView.Adapter<DdayAdapter.DdayViewHolder> {

    private List<Dday> ddayList;

    class DdayViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvDay, tvCount;

        DdayViewHolder (View itemView) {

            super (itemView);

            tvCount    = itemView.findViewById(R.id.tv_count);
            tvDay      = itemView.findViewById(R.id.tv_day);
            tvTitle    = itemView.findViewById(R.id.tv_title);

        }

    }

    public DdayAdapter(List<Dday> ddayList) {

        this.ddayList = ddayList;
    }

    @NonNull
    @Override
    public DdayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dday_item, parent, false);

        return new DdayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DdayViewHolder holder, int position) {

        Dday dday = ddayList.get(position);

        long days = getDays(dday.getDay());

        if (days > 0) {

            holder.tvCount.setText("D - "+ days);
        }
        else
        {
            holder.tvCount.setText("D + "+ Math.abs(days));

        }


        holder.tvDay.setText(dday.getDay());
        holder.tvTitle.setText(dday.getTitle());

    }

    @Override
    public int getItemCount() {
        return ddayList.size();
    }

    private long getDays(String day) {

        long diffDays = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String today = sdf.format(new Date());

        try {
            Date startDate = sdf.parse(today);
            Date endDate = sdf.parse(day);

            long diff = endDate.getTime() - startDate.getTime();
            diffDays = diff / ( 24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return diffDays;
    }
}
