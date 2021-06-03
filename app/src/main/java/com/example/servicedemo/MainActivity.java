package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IUpdateUI {
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.btn_next)
    Button btnNext;

    List<Song> mSongs;
    SongService mService;
    boolean mBound = false;

    private ServiceConnection songConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SongService.SongBinder binder = (SongService.SongBinder) service;
            mService = binder.getService();
            mService.setListener(MainActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        addSong();
        startSongService();
    }



    private void startSongService() {
        Intent intent = new Intent(this, SongService.class);
        startService(intent);
        bindService(intent, songConnection, Context.BIND_AUTO_CREATE);
    }

    private void addSong() {
        mSongs = new ArrayList<>();
        mSongs.add(new Song("a", R.raw.a));
        mSongs.add(new Song("b", R.raw.b));
        mSongs.add(new Song("c", R.raw.c));
    }


    @OnClick({R.id.btn_play, R.id.btn_next, R.id.btn_back, R.id.text_name})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                mService.pauseSong();
                break;
            case R.id.btn_next:
                mService.nextSong();
                break;
            case R.id.btn_back:
                mService.backSong();
                break;
            case R.id.text_name:
                mService.setSongs(mSongs);
                break;
        }
    }

    @Override
    public void updateSongName(String name) {
        textName.setText(name);
    }

    @Override
    public void updateButton(boolean isPlaying) {
        if (isPlaying) {
            btnPlay.setText("Play");
        } else {
            btnPlay.setText("Pause");
        }
    }
}