package study.easycalendar.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import study.easycalendar.CalendarActivity;
import study.easycalendar.DetailActivity;
import study.easycalendar.R;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "[easy][AlarmReceiver]";
    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d(TAG, "AlarmReceiver onReceive");

                // intent로부터 전달받은 string
        String type = intent.getStringExtra("type");
        String title = intent.getStringExtra("title");
        int scheduleId = intent.getIntExtra("id", -1);

        Log.d(TAG, "onReceive " + type);
        Log.d(TAG, "onReceive (notification: " + type + ", title: " + title + ", id: " + scheduleId + ")");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(context, DetailActivity.class);
        newIntent.putExtra("id", scheduleId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                                                2,
                                                                newIntent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "channel_id";
        String channelName = "channel_name";
        NotificationChannel notificationChannel = null;
        Notification.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(context, channelId);
        } else {
            builder = new Notification.Builder(context);
        }

        builder.setSmallIcon(R.drawable.ic_launcher_foreground) // 아이콘 설정하지 않으면 오류남
                .setTicker("EasyCalendar 알림") // 상태바에 표시될 한줄 출력
                .setWhen(System.currentTimeMillis())
                .setNumber(1)
                .setContentTitle("EasyCalendar 알림" + " (" + type + ")") // 제목 설정
                .setContentText(title) // 내용 설정
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}
