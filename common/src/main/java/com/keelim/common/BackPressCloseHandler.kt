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
package com.keelim.common

import android.app.Activity
import android.widget.Toast

class BackPressCloseHandler(private val activity: Activity) {
  private var backKeyPressedTime: Long = 0
  private lateinit var toast: Toast

  fun onBackPressed() {
    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
      backKeyPressedTime = System.currentTimeMillis()
      showGuide()
      return
    }
    if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
      activity.finish()
      toast.cancel()
    }
  }

  private fun showGuide() {
    toast = Toast.makeText(
      activity, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
      Toast.LENGTH_SHORT
    )
    toast.show()
  }
}
