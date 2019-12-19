package com.keelim.cnubus.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keelim.cnubus.R;


public class RootFragment extends Fragment {
    private int rootPosition;
    private ListView listView;


    public int rootPosition() {
        return rootPosition;
    }

    public void setRootPosition(int rootPosition) {
        this.rootPosition = rootPosition;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_root, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.root_list);
        Bundle args = getArguments();
        int position = args.getInt("position"); //position 에 따라서 다른 동작을 한다.

        // 0 a 1 b 2 c 3 night
        switch (position) {
            case 0:
                applyList(getResources().getStringArray(R.array.aroot));
            case 1:
                applyList(getResources().getStringArray(R.array.broot));
            case 2:
                applyList(getResources().getStringArray(R.array.croot));
            case 3:
                applyList(getResources().getStringArray(R.array.night1));
        }
    }

    private void applyList(String[] root) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, root);
        listView.setAdapter(adapter);
    }


}
