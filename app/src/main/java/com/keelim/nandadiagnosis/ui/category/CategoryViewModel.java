package com.keelim.nandadiagnosis.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CategoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CategoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("여기는 아직 어떤 기능을 넣을지는 생각 안했음.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}