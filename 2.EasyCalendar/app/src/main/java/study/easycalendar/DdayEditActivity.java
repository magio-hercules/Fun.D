package study.easycalendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import study.easycalendar.model.Schedule;
import study.easycalendar.model.local.DatabaseHandler;

public class DdayEditActivity extends AppCompatActivity implements ColorPickerDialogListener
{

    private static final String TAG = "DdayEditActivity";

    private static final int SELECT_PICTURE = 1234;

    Calendar myCalendar;
    long minDate;

    int backColor = -26624;

    DatePickerDialog.OnDateSetListener date;

    TextView tvCount, tvDay;
    EditText etTitle;

    LinearLayout itemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday_edit);

        tvCount = findViewById(R.id.tv_count);
        tvDay   = findViewById(R.id.tv_day);
        etTitle = findViewById(R.id.et_title);

        itemLayout = findViewById(R.id.layout_item);

        itemLayout.setBackgroundColor(backColor);


        Calendar cal = new GregorianCalendar();

        cal.add(Calendar.DATE, 1);

        minDate = cal.getTime().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        long days = getDays(sdf.format(cal.getTime()));

        tvCount.setText("D - "+ days);

        tvDay.setText(sdf.format(cal.getTime()));



        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                StringBuilder sb = new StringBuilder();

                sb.append(String.valueOf(year));
                sb.append(String.format("%02d", monthOfYear + 1));
                sb.append(String.format("%02d", dayOfMonth));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                long days = getDays(sb.toString());

                tvCount.setText("D - "+ days);

                tvDay.setText(sb.toString());
            }

        };

        ImageView ivCalendar = findViewById(R.id.iv_calendar);
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createDatePicker();

            }
        });

        tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker();
            }
        });

        tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePicker();

            }
        });


        ImageView ivOk = findViewById(R.id.iv_ok);

        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etTitle.getText())) {

                    Toast.makeText(getApplicationContext(), "D-Day 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Schedule schedule = new Schedule(LocalDate.now()
                                , LocalTime.now()
                                , LocalDate.now()
                                , LocalTime.now()
                                , etTitle.getText().toString()
                                , ""
                                , ""
                                , ""
                                , ""
                                , true,
                                null
                        );

                        DatabaseHandler.getInstance().insertSchedule(schedule);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DdayEditActivity.this, "D-Day 생성 완료", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).start();

                finish();

            }
        });

        ImageView ivColor = findViewById(R.id.iv_color);
        ivColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //ColorPickerDialog.newBuilder() .setDialogType(ColorPickerDialog.TYPE_CUSTOM) .setAllowPresets(false) .setDialogId(DIALOG_DEFAULT_ID) .setColor(Color.BLACK) .setShowAlphaSlider(true) .show(this);

                ColorPickerDialog.newBuilder().show(DdayEditActivity.this);

            }
        });

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView ivImage = findViewById(R.id.iv_image);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    private void createDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(DdayEditActivity.this
                , date
                , myCalendar.get(Calendar.YEAR)
                , myCalendar.get(Calendar.MONTH)
                , myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(minDate);

        datePickerDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                Bitmap bmImg = null;
                Bitmap resized = null;

                try {

                    bmImg = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                } catch (IOException e) {

                    e.printStackTrace();
                }

                int height = bmImg.getHeight();
                int width = bmImg.getWidth();

                resized = Bitmap.createScaledBitmap(bmImg, 300, height/(width/300), true);

                BitmapDrawable background = new BitmapDrawable(resized);
                itemLayout.setBackgroundDrawable(background);
            }
        }
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

    @Override
    public void onColorSelected(int dialogId, int color) {

        //Toast.makeText(getApplicationContext(), "["+dialogId+"]"+ color , Toast.LENGTH_SHORT).show();

        backColor = color;

        itemLayout.setBackgroundColor(backColor);

    }

    @Override
    public void onDialogDismissed(int dialogId) {
       // Toast.makeText(getApplicationContext(), "id => "+ dialogId, Toast.LENGTH_SHORT).show();

    }
}
