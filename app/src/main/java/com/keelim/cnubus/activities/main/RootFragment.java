package com.keelim.cnubus.activities.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.keelim.cnubus.R;


public class RootFragment extends Fragment {
    private int rootPosition;

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
        Bundle args = getArguments();
        int position = args.getInt("position"); //position 에 따라서 다른 동작을 한다.
        // 0 a 1 b 2 c 3 night
        setRootPosition(position);
        switch (rootPosition()) {
            case 0:
                String[] aroot = getResources().getStringArray(R.array.aroot);
            case 1:
                String[] broot = getResources().getStringArray(R.array.broot);
            case 2:
                String[] croot = getResources().getStringArray(R.array.croot);
            case 3:
                String[] night = getResources().getStringArray(R.array.night1);
        }

        Toast.makeText(view.getContext(), "hello" + position, Toast.LENGTH_SHORT).show();
    }


}
