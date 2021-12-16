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
package com.keelim.player.custom

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.keelim.player.R

class CustomMotionLayout(ctx: Context, attributeSet: AttributeSet? = null) : MotionLayout(ctx, attributeSet) {

  private var motionTouchStarted = false
  private val mainContainerView by lazy {
    findViewById<View>(R.id.mainContainerLayout)
  }
  private val hitRect = Rect()

  init {
    setTransitionListener(object : TransitionListener {
      override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

      override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

      override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        motionTouchStarted = false
      }

      override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    })
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        motionTouchStarted = false
        return super.onTouchEvent(event)
      }
    }

    if (!motionTouchStarted) {
      mainContainerView.getHitRect(hitRect)
      motionTouchStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
    }

    return super.onTouchEvent(event) && motionTouchStarted
  }

  private val gestureListener by lazy {
    object : GestureDetector.SimpleOnGestureListener() {
      override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        mainContainerView.getHitRect(hitRect)
        return hitRect.contains(e1.x.toInt(), e1.y.toInt())
      }
    }
  }

  private val gestureDetector by lazy {
    GestureDetector(context, gestureListener)
  }

  override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
    return gestureDetector.onTouchEvent(event)
  }
}
