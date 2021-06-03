package com.example.servicedemo.observable;

public interface INotiObservable {
    void registerObserverNoti(INotiUpdate notiUpdate);

    void notifyService(int type);
}
