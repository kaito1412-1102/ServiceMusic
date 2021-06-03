package com.example.servicedemo.observable;

import java.util.ArrayList;

/*Observable có tác dụng giao tiếp giữa broadcast và Service thông qua notification
Service sẽ implement interface(INotiUpdate), khởi tạo đối tượng  NotificationObservable và đăng ký
interface  với class NotificationObservable thông qua hàm registerObserverNoti
Khi click vào 1 nút bất kì trên notification (next, back, pause) sẽ gửi 1 pendding intent đến cho BroadcastReceiver.
Broadcast sẽ sử dụng đối tượng NotificationObservable để thông báo cho Service biết sự kiện chuẩn bị diễn ra.*/
public class NotificationObservable implements INotiObservable {
    ArrayList<INotiUpdate> mNotiUpdates;
    private static NotificationObservable INSTANCE = null;

    private NotificationObservable() {
        this.mNotiUpdates = new ArrayList<>();
    }

    public static NotificationObservable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NotificationObservable();
        }
        return INSTANCE;
    }

    @Override
    public void registerObserverNoti(INotiUpdate notiUpdate) {
        if (!mNotiUpdates.contains(notiUpdate)) {
            mNotiUpdates.add(notiUpdate);
        }
    }

    @Override
    public void notifyService(int type) {
        for (INotiUpdate notiUpdate : mNotiUpdates) {
            notiUpdate.updateNoti(type);
        }
    }
}
