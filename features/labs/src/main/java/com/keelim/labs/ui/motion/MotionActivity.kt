package com.keelim.labs.ui.motion

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.keelim.cnubus.labs.databinding.ActivityMotionBinding

class MotionActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMotionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.container.setOnTouchListener { _, motion:MotionEvent ->
            handleTouch(motion)
            true
        }
    }

    private fun handleTouch(m:MotionEvent){
        val pointerCount = m.pointerCount
        for(i in 0 until pointerCount){
            val x = m.x
            val y = m.y
            val id = m.getPointerId(i)
            val action = m.actionMasked
            val actionIndex = m.actionIndex
            var actionString: String = when(action){
                MotionEvent.ACTION_DOWN -> "Down"
                MotionEvent.ACTION_UP -> "Up"
                MotionEvent.ACTION_POINTER_DOWN -> "Pointer Down"
                MotionEvent.ACTION_POINTER_UP -> "Pointer Up"
                MotionEvent.ACTION_MOVE -> "MOVE"
                else -> ""
            }
            val state = "Action $actionString Index $actionIndex Id: X: $x  Y: $y"
            if(id == 0){
                binding.textView1.text = state
            } else{
                binding.textView2.text = state
            }

        }
    }
}