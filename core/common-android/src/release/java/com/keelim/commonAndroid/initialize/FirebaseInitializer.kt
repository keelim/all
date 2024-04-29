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
package com.keelim.commonAndroid.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.Firebase
// import com.google.firebase.appcheck.appCheck
// import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize

class FirebaseInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Firebase.initialize(context)
        // Firebase.appCheck.installAppCheckProviderFactory(
        //     PlayIntegrityAppCheckProviderFactory.getInstance(),
        // )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
