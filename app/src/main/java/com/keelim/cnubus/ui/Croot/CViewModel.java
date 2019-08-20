package com.keelim.cnubus.ui.Croot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CViewModel extends ViewModel {

private MutableLiveData<String> mText;

public CViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("C 노선은 개발 중입니다.");
        }

public LiveData<String> getText() {
        return mText;
        }
        }