package com.keelim.nandadiagnosis.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R

class OpenSourceFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(activity, "실험 성공", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_open_source, container, false)

    }
}