package com.keelim.nandadiagnosis.ui.main.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding
import com.keelim.nandadiagnosis.ui.diagnosis.DiagnosisActivity

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private var fragmentCategoryBinding: FragmentCategoryBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCategoryBinding.bind(view)
        fragmentCategoryBinding = binding

        binding.card1.setOnClickListener { showDialog("1") }
        binding.card2.setOnClickListener { showDialog("2") }
        binding.card3.setOnClickListener { showDialog("3") }
        binding.card4.setOnClickListener { showDialog("4") }
        binding.card5.setOnClickListener { showDialog("5") }
        binding.card6.setOnClickListener { showDialog("6") }
        binding.card7.setOnClickListener { showDialog("7") }
        binding.card8.setOnClickListener { showDialog("8") }
        binding.card9.setOnClickListener { showDialog("9") }
        binding.card10.setOnClickListener { showDialog("10") }
        binding.card11.setOnClickListener { showDialog("11") }
        binding.card12.setOnClickListener { showDialog("12") }
        binding.card13.setOnClickListener { showDialog("13") }


    }

    private fun showDialog(num: String) { //데이터를 사용하는 페이지 이니 조심하라는 문구
        AlertDialog.Builder(requireActivity())
                .setMessage("이 기능은 데이터를 사용할 수 있습니다.")
                .setCancelable(false)
                .setPositiveButton("예") { _, _ -> intentList(num) }
                .setNegativeButton("아니오") { _, _ -> Toast.makeText(requireActivity(), "아니오 선택했습니다.", Toast.LENGTH_LONG).show() }
                .create()
                .show()
    }

    private fun intentList(num: String) {
        Intent(requireActivity(), DiagnosisActivity::class.java).apply {
            putExtra("extra", num)
            startActivity(this)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onDestroyView() {
        fragmentCategoryBinding = null
        super.onDestroyView()
    }
}