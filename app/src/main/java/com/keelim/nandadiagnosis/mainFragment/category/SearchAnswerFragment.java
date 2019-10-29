package com.keelim.nandadiagnosis.mainFragment.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keelim.nandadiagnosis.R;

import java.util.ArrayList;

public class SearchAnswerFragment extends Fragment {
    private RecyclerAdapter adapter;

    private ArrayList<String> diagnosis;

    public ArrayList<String> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(ArrayList<String> diagnosis) {
        this.diagnosis = diagnosis;
    }
//프레그먼트에 값전달 --> 이미지는 뺴고 ArrayList 2개 받아와서 정리 하는 것이 깔급
    //Title, Description
    //Data 처리를 할 것

    public static Fragment newInstance(ArrayList<String> title) {
        SearchAnswerFragment fragment = new SearchAnswerFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("TITLE", title);
        fragment.setArguments(args);
        return fragment;
    }


    void DataSetting() {
        //data를 셋팅을 해서 넣으면 된다.
        for (int i = 0; i < diagnosis.size(); i++) {
            Data data = new Data(diagnosis.get(i));
            adapter.addItem(data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            setDiagnosis(bundle.getStringArrayList("title"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_answer_list, container, false);

        RecyclerView recyclerView = getView().findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        return view; //리사이클러 뷰를 셋팅을 한다.
    }


}
