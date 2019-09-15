package com.keelim.nandadiagnosis.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {
    FragmentSearchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        binding.layoutSearchTable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.search_view_1:
                        toastMaker("건강증진 Health promotion");
                        break;
                    case R.id.search_view_2:
                        toastMaker("영양 Nutrition");
                        break;
                    case R.id.search_view_3:
                        toastMaker("배설 Elimination and exchange");
                        break;
                    case R.id.search_view_4:
                        toastMaker("활동/휴식 Activity/Rest");
                        break;
                    case R.id.search_view_5:
                        toastMaker("지각/인지 Perception/Cognition");
                        break;
                    case R.id.search_view_6:
                        toastMaker("자아인식 Self_perception");
                        break;
                    case R.id.search_view_7:
                        toastMaker("역할 관계 Role Relationships");
                        break;
                    case R.id.search_view_8:
                        toastMaker("성 Sexuality");
                        break;
                    case R.id.search_view_9:
                        toastMaker("대응/스트레스 내성 Coping/Stress Tolerance");
                        break;
                    case R.id.search_view_10:
                        toastMaker("생의 원리 Life Principles");
                        break;
                    case R.id.search_view_11:
                        toastMaker("안정/보호 Safety/Promotion");
                        break;
                    case R.id.search_view_12:
                        toastMaker("안위 Comfort");
                        break;
                    default:

                }
            }
        });

        return binding.getRoot();
    }
    // private function
    private void toastMaker(String NANDA_DOMAIN){
        Toast.makeText(getContext(), NANDA_DOMAIN, Toast.LENGTH_SHORT).show();
    }



}
