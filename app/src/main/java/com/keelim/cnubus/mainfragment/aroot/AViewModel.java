package com.keelim.cnubus.mainfragment.aroot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("A root 노선 개발 중");
    }

    public LiveData<String> getText() {
        return mText;
    }
}