package com.keelim.cnubus.ui.root.aroot


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentARootBinding
import com.keelim.cnubus.feature.map.MapsActivity
import com.keelim.cnubus.feature.map.StationActivity

class ARootFragment : Fragment(R.layout.fragment_a_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>
    private var fragmentARootBinding: FragmentARootBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentARootBinding.bind(view)
        fragmentARootBinding = binding
        rootList = resources.getStringArray(R.array.aroot)
        intentList = resources.getStringArray(R.array.a_intent_array)

        binding.lvAroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

        binding.lvAroot.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(activity, rootList[i] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }
    }

    override fun onDestroyView() {
        fragmentARootBinding = null
        super.onDestroyView()
    }
}