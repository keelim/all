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
package com.keelim.nandadiagnosis.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

internal abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {
  abstract val viewModel: VM
  protected lateinit var binding: VB

  abstract fun getViewBinding(): VB

  private lateinit var fetchJob: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = getViewBinding()
    setContentView(binding.root)

    fetchJob = viewModel.fetchData()
    observeData()
  }

  abstract fun observeData()

  override fun onDestroy() {
    if (fetchJob.isActive) {
      fetchJob.cancel()
    }
    super.onDestroy()
  }
}
