package com.example.servicedemo.observable;

//Service implement interface này để nhận sự kiện click từ notification (back,next,play,pause)
public interface INotiUpdate {
    void updateNoti(int type);
}
