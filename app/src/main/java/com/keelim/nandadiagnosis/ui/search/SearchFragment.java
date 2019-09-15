package com.keelim.nandadiagnosis.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                        break;
                    case R.id.search_view_2:
                        break;
                    case R.id.search_view_3:
                        break;
                    case R.id.search_view_4:
                        break;
                    case R.id.search_view_5:
                        break;
                    case R.id.search_view_6:
                        break;
                    case R.id.search_view_7:
                        break;
                    case R.id.search_view_8:
                        break;
                    case R.id.search_view_9:
                        break;
                    case R.id.search_view_10:
                        break;
                    case R.id.search_view_11:
                        break;
                    case R.id.search_view_12:
                        break;
                    default:

                }


            }
        });

        return binding.getRoot();
    }


}
