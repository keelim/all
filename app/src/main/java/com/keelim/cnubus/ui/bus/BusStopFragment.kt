package com.keelim.cnubus.ui.bus

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentBusStopBinding
import com.keelim.common.base.BaseFragment

class BusStopFragment : BaseFragment<FragmentBusStopBinding, BusStopViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_bus_stop
    override val viewModel: BusStopViewModel by viewModels()

    override fun initBeforeBinding() {
        TODO("Not yet implemented")
    }

    override fun initBinding() {
        TODO("Not yet implemented")
    }

    override fun initAfterBinding() {
        TODO("Not yet implemented")
    }

    companion object{
        fun newInstance():BusStopFragment {
            return BusStopFragment().apply {
                arguments = bundleOf()
            }
        }

        /**
         * @parm uuid is bus stop name
         */
        fun newInstance(uuid:String?):BusStopFragment{
            return BusStopFragment().apply {
                uuid?.let{
                    arguments = bundleOf(
                        "uuid" to uuid
                    )
                }
            }
        }
    }
}