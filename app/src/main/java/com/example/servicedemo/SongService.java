package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.servicedemo.observable.INotiObservable;
import com.example.servicedemo.observable.INotiUpdate;
import com.example.servicedemo.observable.NotificationObservable;
import com.example.servicedemo.notification.SongNotification;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class SongService extends Service implements INotiUpdate {
    private final IBinder binder = new SongBinder();
    private IUpdateUI mListener;
    private List<Song> mSongs;
    int position = 0;
    MediaPlayer mediaPlayer;
    public INotiObservable notiObservable;
    SongNotification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        Log.d("TAGG", "onCreate: ");
        mSongs = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notiObservable = NotificationObservable.getInstance();
        notiObservable.registerObserverNoti(this);
        notification = new SongNotification(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    void nextSong() {
        mediaPlayer.pause();
        position++;
        if (position == mSongs.size())
            position = 0;
        initMedia(position);
        mediaPlayer.start();
        notification.nextBackSong(mSongs.get(position).getTitle());
    }

    void backSong() {
        mediaPlayer.pause();
        position--;
        if (position == -1)
            position = mSongs.size() - 1;
        initMedia(position);
        mediaPlayer.start();
        notification.nextBackSong(mSongs.get(position).getTitle());
    }

    void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mListener.updateButton(false);
            notification.playNotification(false);
            stopForeground(false);
        } else {
            mediaPlayer.start();
            mListener.updateButton(true);
            notification.playNotification(true);
            startForeground(1, notification.getNotification());
        }
    }

    private void initMedia(int pos) {
        mediaPlayer = MediaPlayer.create(this, mSongs.get(pos).getFile());
        mediaPlayer.start();
        mListener.updateSongName(mSongs.get(pos).getTitle());
        mListener.updateButton(true);
        startForeground(1, notification.getNotification());
        notification.nextBackSong(mSongs.get(pos).getTitle());
    }

    void setSongs(List<Song> songs) {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        mSongs = songs;
        initMedia(position);
    }

    void setListener(IUpdateUI listener) {
        mListener = listener;
    }

    @Override
    public void updateNoti(int type) {
        switch (type) {
            case Constant.BACK_SONG:
                backSong();
                break;
            case Constant.NEXT_SONG:
                nextSong();
                break;
            case Constant.PLAY_PAUSE_SONG:
                pauseSong();
                break;
        }
    }

    public class SongBinder extends Binder {
        SongService getService() {
            return SongService.this;
        }
    }
}
