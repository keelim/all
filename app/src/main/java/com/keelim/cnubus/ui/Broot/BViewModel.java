package com.keelim.cnubus.ui.Broot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("B 노선은 개발 중입니다.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}