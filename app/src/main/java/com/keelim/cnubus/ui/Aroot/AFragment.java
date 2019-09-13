package com.keelim.cnubus.ui.Aroot;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.cnubus.R;
import com.keelim.cnubus.databinding.FragmentArootBinding;

import java.util.Date;

public class AFragment extends Fragment {
    private String basic_string;
    private FragmentArootBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AViewModel aViewModel = ViewModelProviders.of(this).get(AViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_aroot, container, false);
        View root = binding.getRoot();
        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.aList,
                android.R.layout.simple_list_item_1);

        binding.aListview.setAdapter(arrayAdapterA);
        basic_string = binding.currentTimeA.getText().toString();
        binding.floatingActionButtona.setOnClickListener(view -> {
            String add_date = getDate();
            binding.currentTimeA.setText(String.format("%s%s", basic_string, add_date));
        });

        return root;
    }

    private String getDate() {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        return sdfNow.format(date);
    }
    // inner function


}