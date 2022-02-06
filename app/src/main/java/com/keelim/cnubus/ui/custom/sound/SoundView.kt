package com.keelim.cnubus.ui.custom.sound

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.keelim.cnubus.R

class SoundView(
    ctx: Context,
    attributeSet: AttributeSet? = null
) : View(ctx, attributeSet) {

    var requestAction: (() -> Int)? = null

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ctx.getColor(R.color.black)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND // 라인의 양 끝을 동그랗게
    }

    private var drawingWidth = 0
    private var drawingHeight = 0
    private var isReplaying = false
    private var replayingPosition = 0

    var drawingWaves = emptyList<Int>()

    private val waveAction = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                val currentAmplitude = requestAction?.invoke() ?: 0
                drawingWaves = drawingWaves + listOf(currentAmplitude)
            } else {
                replayingPosition++
            }
            invalidate() // 드로잉 처리

            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val centerY = drawingHeight / 2f  // 뷰의 중앙 높이
        var offsetX = drawingWidth.toFloat()  // 오른 쪽 부터

        // 진폭값을 배열로 넣어두고 오른쪽 부터 왼쪽으로 그려지게 함
        drawingWaves
            .let { amplitudes ->
                if (isReplaying) {
                    amplitudes.takeLast(replayingPosition) // 가장 뒤 부터 리플레이 포지션 까지
                } else {
                    amplitudes
                }
            }
            .forEach { amplitude ->
                // 그릴려는 높이 대비 몇퍼로 그릴지 (80%)
                val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F

                // offset 계산 다시 하고 뷰의 우측 부터 그릴 것임.
                offsetX -= LINE_SPACE

                // 진폭 배열이 많이 쌓여 뷰를 초과하는 경우 혹은 보이지 않는 경우, 뷰를 그리지 않아야함
                if (offsetX < 0) return@forEach

                // 진폭 그리기 (좌상단이 0, 0 우하단이 w, h)
                canvas.drawLine(
                    offsetX, // 시작
                    centerY - lineLength / 2F,
                    offsetX,
                    centerY + lineLength / 2F,
                    wavePaint
                )
            }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(waveAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0 // 구간 초기화
        handler?.removeCallbacks(waveAction)
    }

    fun clearVisualization() {
        drawingWaves = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }

}