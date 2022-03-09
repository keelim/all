package com.keelim.cnubus.ui.table

import android.view.Gravity
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.zardozz.FixedHeaderTableLayout.FixedHeaderSubTableLayout
import com.github.zardozz.FixedHeaderTableLayout.FixedHeaderTableRow
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentTableBinding
import com.keelim.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.String
import java.util.Locale


@AndroidEntryPoint
class TableFragment : BaseFragment<FragmentTableBinding, TableViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_table
    override val viewModel: TableViewModel by viewModels()

    override fun initBeforeBinding() {
        val mainTable = FixedHeaderSubTableLayout(context)
        for (i in 1..25) {
            val tableRowData = FixedHeaderTableRow(context)
            for (j in 1..25) {
                val textView = TextView(context)
                textView.gravity = Gravity.CENTER
                textView.text = String.format(Locale.ROOT, "C%d:R%d", j, i)
                textView.setPadding(5, 5, 5, 5)
                textView.textSize = 20.0f
                tableRowData.addView(textView)
            }
            // Add row to the Table
            mainTable.addView(tableRowData)
        }
        binding.FixedHeaderTableLayout.addView(mainTable)
    }

    override fun initBinding() = Unit

    override fun initAfterBinding() = Unit

    companion object {
        fun newInstance(): Fragment {
            return TableFragment().apply {
                arguments = bundleOf(

                )
            }
        }
    }
}