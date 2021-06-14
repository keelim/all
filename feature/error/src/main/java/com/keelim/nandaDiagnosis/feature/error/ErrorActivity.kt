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
package com.keelim.nandaDiagnosis.feature.error

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keelim.nandaDiagnosis.feature.error.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {
  private val binding by lazy { ActivityErrorBinding.inflate(layoutInflater) }
  private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
  private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.tvErrorLog.text = errorText
    binding.btnReload.setOnClickListener {
      startActivity(lastActivityIntent)
      finish()
    }
  }

  companion object {
    const val EXTRA_INTENT = "EXTRA_INTENT"
    const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
  }
}
