package com.example.servicedemo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.servicedemo.R;

import androidx.annotation.IdRes;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SongNotification {
    public static final String EXTRA_BUTTON_CLICKED = "EXTRA_BUTTON_CLICKED";
    private static final String CHANNEL_ID = "Song_Notificaction";
    //    private NotificationManager notificationManager;
    private static RemoteViews remoteViews;
    private final NotificationManager notificationManager;
    Notification notification;
    private Context context;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanel_name";
            String description = "song_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public SongNotification(Context ctx) {
        context = ctx;
        createNotificationChannel();
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
//        remoteViews.setImageViewResource(R.id.img_noti, R.drawable.noti_icon);
        remoteViews.setOnClickPendingIntent(R.id.btn_back, onButtonNotificationClick(R.id.btn_back));
        remoteViews.setOnClickPendingIntent(R.id.btn_play, onButtonNotificationClick(R.id.btn_play));
        remoteViews.setOnClickPendingIntent(R.id.btn_next, onButtonNotificationClick(R.id.btn_next));
    }

    public Notification getNotification() {
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.noti_icon)
                .setCustomContentView(remoteViews)
                .setOngoing(false)
                .build();
        return notification;
    }

    public void playNotification(boolean isPlaying) {
        if (isPlaying) {
            remoteViews.setTextViewText(R.id.btn_play, "play");
        } else {
            remoteViews.setTextViewText(R.id.btn_play, "pause");
        }
        notificationManager.notify(1, notification);
    }

    public void nextBackSong(String name) {
        remoteViews.setTextViewText(R.id.text_name, name);
        remoteViews.setTextViewText(R.id.btn_play, "play");
        notificationManager.notify(1, notification);
    }

    private PendingIntent onButtonNotificationClick(@IdRes int id) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(EXTRA_BUTTON_CLICKED, id);
        return PendingIntent.getBroadcast(context, id, intent, 0);
    }
}
