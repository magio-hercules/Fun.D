package com.fundroid.offstand.ui.room;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fundroid.offstand.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Room2Activity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
    static final String TAG = "[ROOM]";

    @BindView(R.id.btn_table)
    Button btn_table;
    @BindView(R.id.btn_ban)
    Button btn_ban;
    @BindView(R.id.btn_shuffle)
    Button btn_shuffle;
    @BindView(R.id.btn_ready)
    Button btn_ready;
    @BindView(R.id.btn_1)
    Button btn_1;
    @BindView(R.id.btn_2)
    Button btn_2;
    @BindView(R.id.btn_3)
    Button btn_3;
    @BindView(R.id.btn_4)
    Button btn_4;
    @BindView(R.id.btn_5)
    Button btn_5;
    @BindView(R.id.btn_6)
    Button btn_6;
    @BindView(R.id.btn_7)
    Button btn_7;
    @BindView(R.id.btn_8)
    Button btn_8;
    @BindView(R.id.btn_9)
    Button btn_9;
    @BindView(R.id.btn_10)
    Button btn_10;
    @BindView(R.id.btn_11)
    Button btn_11;
    @BindView(R.id.btn_12)
    Button btn_12;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room10);

        // 버터나이프 사용
        ButterKnife.bind(this);

        btn_shuffle.setOnTouchListener(this);
        btn_ban.setOnTouchListener(this);
        btn_ready.setOnTouchListener(this);

        btn_shuffle.setOnDragListener(this);
        btn_ban.setOnDragListener(this);
        btn_ready.setOnDragListener(this);

        btn_1.setOnTouchListener(this);
        btn_2.setOnTouchListener(this);
        btn_3.setOnTouchListener(this);
        btn_4.setOnTouchListener(this);
        btn_5.setOnTouchListener(this);
        btn_6.setOnTouchListener(this);
        btn_7.setOnTouchListener(this);
        btn_8.setOnTouchListener(this);
        btn_9.setOnTouchListener(this);
        btn_10.setOnTouchListener(this);
        btn_11.setOnTouchListener(this);
        btn_12.setOnTouchListener(this);

        btn_1.setOnDragListener(this);
        btn_2.setOnDragListener(this);
        btn_3.setOnDragListener(this);
        btn_4.setOnDragListener(this);
        btn_5.setOnDragListener(this);
        btn_6.setOnDragListener(this);
        btn_7.setOnDragListener(this);
        btn_8.setOnDragListener(this);
        btn_9.setOnDragListener(this);
        btn_10.setOnDragListener(this);
        btn_11.setOnDragListener(this);
        btn_12.setOnDragListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

        int eventPadTouch = event.getAction();

        switch (eventPadTouch) {
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()) {
                    case R.id.btn_shuffle:
                        Toast.makeText(getApplicationContext(), "셔플", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_ready:
                        Toast.makeText(getApplicationContext(), "준비", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_ban:
                        Toast.makeText(getApplicationContext(), "내보내기", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.btn_1:
                    case R.id.btn_2:
                    case R.id.btn_3:
                    case R.id.btn_4:
                    case R.id.btn_5:
                    case R.id.btn_6:
                    case R.id.btn_7:
                    case R.id.btn_8:
                    case R.id.btn_9:
                    case R.id.btn_10:
                    case R.id.btn_11:
                    case R.id.btn_12:
                        if (((Button) v).getText().equals("")) {
                            Log.d(TAG, "빈 자리");
                            return false;
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            v.startDragAndDrop(data, mShadow, v, 0);
                        } else {
                            v.startDrag(data, mShadow, null, 0);
                        }
                        break;
                }
                break;
        }



        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
//        String clipData = event.getClipDescription().getLabel().toString();
        Button target = (Button)v;
        Button selected = (Button) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
//                ((ImageView) v).setColorFilter(Color.YELLOW);
//
//                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                String targetStr = target.getText().toString();
                Log.d(TAG, "ACTION_DRAG_ENTERED : " + targetStr);
                if (targetStr.equals("내보내기")) {
                    target.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (targetStr.equals("준비")) {
                    target.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    String clipData = event.getClipDescription().getLabel().toString();
                    Log.d(TAG, "clipData : " + clipData);
                    switch (clipData) {
//                        case "BAN":
//                            break;
//                        case "READY":
//                            break;
                        case "USER":
                            Log.d(TAG, "ACTION_DRAG_ENTERED USER");
                            target.setBackgroundColor(getResources().getColor(R.color.green));
                            break;
                    }

                    v.invalidate();
                }
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                targetStr = target.getText().toString();
                Log.d(TAG, "ACTION_DRAG_EXITED : " + targetStr);
                if (targetStr.equals("내보내기")) {
                    target.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else if (targetStr.equals("준비")) {
                    target.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    ClipDescription clipDesc = (ClipDescription)event.getClipDescription();
                    if (clipDesc == null) {
                        return false;
                    }

                    String clipData = clipDesc.getLabel().toString();
                    switch (clipData) {
                        case "USER":
                            ((Button) v).setBackgroundResource(android.R.drawable.btn_default);
                            break;
                    }
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                targetStr = target.getText().toString();
                Log.d(TAG, "ACTION_DRAG_ENDED : " + targetStr);
                if (targetStr.equals("내보내기")) {
                    target.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
                } else if (targetStr.equals("준비")) {
                    target.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
                } else {
                    ClipDescription clipDesc = (ClipDescription)event.getClipDescription();
                    if (clipDesc == null) {
                        return false;
                    }

                    String clipData = clipDesc.getLabel().toString();
                    switch (clipData) {
                        case "USER":
                            ((Button) v).setBackgroundResource(android.R.drawable.btn_default);
                            break;
                    }
                }
                return true;

            case DragEvent.ACTION_DROP:
                targetStr = target.getText().toString();
                Log.d(TAG, "ACTION_DROP : " + targetStr);
                if (targetStr.equals("내보내기")) {
                    target.setBackgroundColor(getResources().getColor(R.color.holo_orange_light));
                    doBan(selected);
                } else if (targetStr.equals("준비")) {
                    target.setBackgroundColor(getResources().getColor(R.color.holo_orange_light));
                    doReady(selected);
                } else {
                    doChange(selected, target);
                }
                return true;

            default:
                return false;
        }
    }

    private void doChange(Button selected, Button target) {
        String targetText = target.getText().toString();
        String draggedText = selected.getText().toString();

        selected.setText(targetText);
        target.setText(draggedText);

        Toast.makeText(getApplicationContext(), "자리 변경", Toast.LENGTH_SHORT).show();
    }

    private void doReady(Button selected) {
        Toast.makeText(getApplicationContext(), "준비", Toast.LENGTH_SHORT).show();
    }

    private void doBan(Button selected) {
        selected.setText("");

        Toast.makeText(getApplicationContext(), "내보내기", Toast.LENGTH_SHORT).show();
    }
}
