package com.keelim.nandadiagnosis.view.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.view.DiagnosisActivity
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment(R.layout.fragment_category), View.OnClickListener {

    private fun showDialog(num: String) { //데이터를 사용하는 페이지 이니 조심하라는 문구
        AlertDialog.Builder(activity!!)
                .setMessage("이 기능은 데이터를 사용할 수 있습니다.\n 사용하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예") { _, _ -> intentList(num) }
                .setNegativeButton("아니오") { _, _ -> Toast.makeText(activity!!, "아니오 선택했습니다.", Toast.LENGTH_LONG).show() }
                .create()
                .show()
    }

    private fun intentList(num: String) {
        Intent(activity!!, DiagnosisActivity::class.java).apply {
            putExtra("extra", num)
            startActivity(this)
        }
    }

    override fun onClick(v: View?) {
        when (v!!) {
            search_view_1 -> showDialog("1")
            search_view_2 -> showDialog("2")
            search_view_3 -> showDialog("3")
            search_view_4 -> showDialog("4")
            search_view_5 -> showDialog("5")
            search_view_6 -> showDialog("6")
            search_view_7 -> showDialog("7")
            search_view_8 -> showDialog("8")
            search_view_9 -> showDialog("9")
            search_view_10 -> showDialog("10")
            search_view_11 -> showDialog("11")
            search_view_12 -> showDialog("12")
            search_view_13 -> showDialog("13")
        }
    }


}