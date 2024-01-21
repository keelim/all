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
package com.keelim.cnubus.ui.screen.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.deeplinkdispatch.DeepLink
import com.keelim.cnubus.ui.CnubusApp
import com.keelim.commonAndroid.core.AppMainDelegator
import com.keelim.commonAndroid.core.AppMainViewModel
import com.keelim.composeutil.setThemeContent
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@DeepLink("all://screen/{name}")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: AppMainViewModel by viewModels()
    private val appMainDelegator by lazy { AppMainDelegator(this, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setThemeContent {
            CnubusApp(
                windowSizeClass = calculateWindowSizeClass(this),
            )

            var isDialogOpen by remember { mutableStateOf(false) }
            BackHandler(
                enabled = true,
                onBack = {
                    isDialogOpen = true
                },
            )
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = {
                        isDialogOpen = false
                    },
                    title = { Text(text = "안내") },
                    text = { Text(text = "종료 하시겠습니까?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                finish()
                            },
                        ) {
                            Text(
                                text = "확인",
                            )
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            isDialogOpen = false
                        }) {
                            Text(
                                text = "취소",
                            )
                        }
                    },
                )
            }
        }
    }
}
