package study.easycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import study.easycalendar.adapter.DdayAdapter;
import study.easycalendar.model.Dday;

public class DdayActivity extends AppCompatActivity {

    private List<Dday> ddayList;

    private DdayAdapter adapter;

    private RecyclerView rvDday, rvCounting;

    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ddayList = new ArrayList<>();

        rvDday = findViewById(R.id.rv_dday);
        rvCounting = findViewById(R.id.rv_counting);

        rvDday.setLayoutManager(new LinearLayoutManager(this));
        rvCounting.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DdayAdapter(ddayList);
        rvDday.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                ddayList.remove(position);
                adapter.notifyDataSetChanged();
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);




        ImageView newDday = (ImageView) findViewById(R.id.iv_new_dday);

        newDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(getApplicationContext(), DdayEditActivity.class);
                startActivity(intent);

            }
        });

        Button btnDday = (Button) findViewById(R.id.btn_dday);

        btnDday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rvDday.setVisibility(View.VISIBLE);
                rvCounting.setVisibility(View.GONE);

                itemTouchHelper.attachToRecyclerView(rvDday);

                ddayList.clear();


                testDday();
            }
        });

        Button btnCounting = (Button) findViewById(R.id.btn_counting);

        btnCounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rvDday.setVisibility(View.GONE);
                rvCounting.setVisibility(View.VISIBLE);

                itemTouchHelper.attachToRecyclerView(rvCounting);

                ddayList.clear();

                testCounting();
            }
        });
    }


    private void testDday()
    {

        ddayList.add(new Dday("고등어","20190101", -769226,null));
        ddayList.add(new Dday("미꾸라지","20190215", -26624, null));
        ddayList.add(new Dday("갈치","20190330",-3285959, null));

        rvDday.setAdapter(adapter);
    }

    private void testCounting()
    {

        ddayList.add(new Dday("오징어","20180101",-14575885, null));
        ddayList.add(new Dday("꼴뚜기","20180215",-769226,null));
        ddayList.add(new Dday("랍스터","20180330",-26624, null));

        rvCounting.setAdapter(adapter);
    }

}