package com.keelim.nandadiagnosis.ui.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel { //todo LiveData 가 뭔가 -> Room 에서도 쓰이던데
    //todo Viewmodel 사용하는 법 아직 감이 오지는 않는다.

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "여기에 뭐 넣을지 생각을 안해봤습니다.  " + input);

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}