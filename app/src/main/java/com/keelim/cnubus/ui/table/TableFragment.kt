package com.keelim.cnubus.ui.table

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentTableBinding
import com.keelim.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TableFragment : BaseFragment<FragmentTableBinding, TableViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_table
    override val viewModel: TableViewModel by viewModels()

    override fun initBeforeBinding() {
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