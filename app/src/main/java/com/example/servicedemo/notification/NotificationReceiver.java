package com.example.servicedemo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.servicedemo.Constant;
import com.example.servicedemo.R;
import com.example.servicedemo.observable.NotificationObservable;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(SongNotification.EXTRA_BUTTON_CLICKED, -1);
        switch (id) {
            case R.id.btn_back:
                NotificationObservable.getInstance().notifyService(Constant.BACK_SONG);
                break;
            case R.id.btn_play:
                NotificationObservable.getInstance().notifyService(Constant.PLAY_PAUSE_SONG);
                break;
            case R.id.btn_next:
                NotificationObservable.getInstance().notifyService(Constant.NEXT_SONG);
                break;
        }
    }
}