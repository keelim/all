/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.bus

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentBusStopBinding

class BusStopFragment : Fragment(R.layout.fragment_bus_stop) {
    private lateinit var binding: FragmentBusStopBinding
    private val viewModel: BusStopViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBusStopBinding.bind(view)
    }

    companion object {
        fun newInstance(): BusStopFragment {
            return BusStopFragment().apply {
                arguments = bundleOf()
            }
        }

        /**
         * @parm uuid is bus stop name
         */
        fun newInstance(uuid: String?): BusStopFragment {
            return BusStopFragment().apply {
                uuid?.let {
                    arguments = bundleOf(
                        "uuid" to uuid
                    )
                }
            }
        }
    }
}
