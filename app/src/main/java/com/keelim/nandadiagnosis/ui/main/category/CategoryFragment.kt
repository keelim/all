/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
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
package com.keelim.nandadiagnosis.presentation.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.keelim.nandadiagnosis.data.model.Recent
import com.keelim.nandadiagnosis.databinding.FragmentCategoryBinding
import com.keelim.nandadiagnosis.presentation.main.category.recent.RecentAdapter
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

class CategoryFragment : Fragment() {
  private var _binding: FragmentCategoryBinding? = null
  private val binding get() = _binding!!
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView()
    initData()
  }

  private fun initView() {
    with(binding) {
      card1.setOnClickListener { goNext("1") }
      card2.setOnClickListener { goNext("2") }
      card3.setOnClickListener { goNext("3") }
      card4.setOnClickListener { goNext("4") }
      card5.setOnClickListener { goNext("5") }
      card6.setOnClickListener { goNext("6") }
      card7.setOnClickListener { goNext("7") }
      card8.setOnClickListener { goNext("8") }
      card9.setOnClickListener { goNext("9") }
      card10.setOnClickListener { goNext("10") }
      card11.setOnClickListener { goNext("11") }
      card12.setOnClickListener { goNext("12") }
      card13.setOnClickListener { goNext("13") }
    }
  }

  private fun initData() {
    val remoteConfig = Firebase.remoteConfig
    remoteConfig.setConfigSettingsAsync(
      remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0
      }
    )
    remoteConfig.fetchAndActivate().addOnCompleteListener {
      if (it.isSuccessful) {
        val quotes = parseJson(remoteConfig.getString("recents"))
        Timber.d("$quotes")
        displayPager(quotes)
      }
    }
  }

  private fun parseJson(json: String): List<Recent> {
    val jsonArray = JSONArray(json)
    var jsonList = emptyList<JSONObject>()
    for (index in 0 until jsonArray.length()) {
      val jsonObject = jsonArray.getJSONObject(index)
      jsonObject?.let {
        jsonList = jsonList + it
      }
    }

    return jsonList.map {
      Recent(
        reason = it.getString("reason"),
        domain = it.getString("domain"),
        class_name = it.getString("class_name"),
        category = it.getString("category"),
      )
    }
  }

  private fun displayPager(recents: List<Recent>) {
    val recentAdapter = RecentAdapter(recents = recents)
    with(binding.recycler) {
      adapter = recentAdapter
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }
  }

  fun goNext(num: String) { // 데이터를 사용하는 페이지 이니 조심하라는 문구
    Snackbar.make(binding.root, "이 기능은 데이터를 사용할 수 있습니다.", Snackbar.LENGTH_LONG)
      .setAction("ok") {
        findNavController().navigate(
          CategoryFragmentDirections.actionNavigationCategoryToDiagnosisFragment(num)
        )
      }
      .show()
  }
}
