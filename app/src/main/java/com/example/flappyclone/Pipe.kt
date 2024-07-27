package com.example.flappyclone

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas

class Pipe(context: Context, screenWidth: Int, screenHeight: Int, private val pipeBitmap: Bitmap) {
    var x: Float = screenWidth.toFloat()
    private val y: Float = (screenHeight / 2).toFloat()
    private val speed: Float = 5f
    private val pipeWidth: Int = pipeBitmap.width
    private val pipeHeight: Int = pipeBitmap.height

    fun update() {
        x -= speed
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(pipeBitmap, x, y, null)
    }

    fun isOffScreen(): Boolean {
        return x + pipeWidth < 0
    }
}