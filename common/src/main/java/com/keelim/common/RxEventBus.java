package com.keelim.common;

import io.reactivex.subjects.PublishSubject;

public class RxEventBus {
    private static RxEventBus mRxEventBus;
    private final PublishSubject<String> mSubject;


    private RxEventBus() {
        mSubject = PublishSubject.create();
    }

    public static RxEventBus getInstance() {
        if (mRxEventBus == null) {
            mRxEventBus = new RxEventBus();
        }
        return mRxEventBus;
    }

    public void sendEvent(String string) {
        mSubject.onNext(string);
    }

    public PublishSubject<String> getEvents() {
        return mSubject;
    }
}
