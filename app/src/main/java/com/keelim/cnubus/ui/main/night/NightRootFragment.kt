package com.keelim.cnubus.ui.main.night

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentNightRootBinding
import com.keelim.cnubus.ui.MapsActivity

class NightRootFragment : Fragment(R.layout.fragment_night_root) {
    private lateinit var intentList: Array<String>
    private lateinit var rootList: Array<String>
    private var fragmentNightRootBinding: FragmentNightRootBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNightRootBinding.bind(view)
        fragmentNightRootBinding = binding
        rootList = resources.getStringArray(R.array.night1)
        intentList = resources.getStringArray(R.array.night_intent_array)



        /*binding.lvNightroot.setOnItemClickListener { _, _, i, _ ->
            Snackbar.make(binding.root, "기능 준비 중입니다. ", Snackbar.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }*/

        binding.lvNightroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)
    }

    override fun onDestroyView() {
        fragmentNightRootBinding = null
        super.onDestroyView()
    }

}