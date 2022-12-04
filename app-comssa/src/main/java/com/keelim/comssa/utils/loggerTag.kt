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
package com.keelim.comssa.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import javax.inject.Inject
import timber.log.Timber

private const val loggerTag = "Logger"

private inline val <T : Any> T.javaClassName: String
    get() = javaClass.name

private fun Fragment.printLifecycle(lifecycleScope: String) {
    Timber.tag(loggerTag).d("[Fragment] $lifecycleScope - $javaClassName(${hashCode()})")
}

private fun Activity.printLifecycle(lifecycleScope: String) {
    Timber.tag(loggerTag).d("[Activity] $lifecycleScope - $javaClassName(${hashCode()})")
}

class ComponentLogger @Inject constructor() {
    fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activity.printLifecycle("onCreate")
                    handleActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    activity.printLifecycle("onStart")
                }

                override fun onActivityResumed(activity: Activity) {
                    activity.printLifecycle("onResume")
                }

                override fun onActivityPaused(activity: Activity) {
                    activity.printLifecycle("onPause")
                }

                override fun onActivityStopped(activity: Activity) {
                    activity.printLifecycle("onStop")
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                    activity.printLifecycle("onSaveInstance")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    activity.printLifecycle("onDestroy")
                }
            }
        )
    }

    private fun handleActivity(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentPreAttached(
                        fm: FragmentManager,
                        f: Fragment,
                        context: Context
                    ) {
                    }

                    override fun onFragmentCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        super.onFragmentCreated(fm, f, savedInstanceState)
                        f.printLifecycle("onCreate")
                    }

                    override fun onFragmentViewCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        v: View,
                        savedInstanceState: Bundle?
                    ) {
                        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                        f.printLifecycle("onViewCreated")
                    }

                    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                        super.onFragmentStarted(fm, f)
                        f.printLifecycle("onStart")
                    }

                    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentResumed(fm, f)
                        f.printLifecycle("onResume")
                    }

                    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                        super.onFragmentPaused(fm, f)
                        f.printLifecycle("onPause")
                    }

                    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                        super.onFragmentStopped(fm, f)
                        f.printLifecycle("onStop")
                    }

                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentViewDestroyed(fm, f)
                        f.printLifecycle("onViewDestroy")
                    }

                    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentDestroyed(fm, f)
                        f.printLifecycle("onDestroy")
                    }
                },
                true
            )
        }
    }
}
