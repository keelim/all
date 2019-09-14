package com.keelim.nandadiagnosis.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.keelim.nandadiagnosis.R;

public class HomeSecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String myArg = HomeSecondFragmentArgs.fromBundle(getArguments()).getMyArg();
        TextView textView = view.findViewById(R.id.textview_home_second);
        textView.setText(getString(R.string.hello_home_second, myArg));

        view.findViewById(R.id.button_home_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeSecondFragment.this)
                        .navigate(R.id.action_HomeSecondFragment_to_HomeFragment);
            }
        });
    }
}
