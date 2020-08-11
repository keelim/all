package com.keelim.nandadiagnosis.view.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.view.DiagnosisActivity
import kotlinx.android.synthetic.main.fragment_category.view.*


class CategoryFragment : Fragment(R.layout.fragment_category) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireView().search_view_1.setOnClickListener { showDialog("1") }
        requireView().search_view_2.setOnClickListener { showDialog("2") }
        requireView().search_view_3.setOnClickListener { showDialog("3") }
        requireView().search_view_4.setOnClickListener { showDialog("4") }
        requireView().search_view_5.setOnClickListener { showDialog("5") }
        requireView().search_view_6.setOnClickListener { showDialog("6") }
        requireView().search_view_7.setOnClickListener { showDialog("7") }
        requireView().search_view_8.setOnClickListener { showDialog("8") }
        requireView().search_view_9.setOnClickListener { showDialog("9") }
        requireView().search_view_10.setOnClickListener { showDialog("10") }
        requireView().search_view_11.setOnClickListener { showDialog("11") }
        requireView().search_view_12.setOnClickListener { showDialog("12") }
        requireView().search_view_13.setOnClickListener { showDialog("13") }
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
}