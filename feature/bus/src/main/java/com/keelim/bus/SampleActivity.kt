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
package com.keelim.bus

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SampleActivity : Activity() {
  private lateinit var compositeDisposable: CompositeDisposable
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 이벤트를 보낼 때
    RxEventBus.getInstance().sendEvent("testEvent")

    // 이벤트를 받을 때
    compositeDisposable = CompositeDisposable()

    val disposable: Disposable = RxEventBus.getInstance()
      .events
      .subscribe({ t -> Toast.makeText(this@SampleActivity, t, Toast.LENGTH_SHORT).show() }) { Toast.makeText(this@SampleActivity, "error", Toast.LENGTH_SHORT).show() }
    compositeDisposable.addAll(disposable)
  }

  override fun onDestroy() {
    super.onDestroy()
    compositeDisposable.dispose()
  }
}
