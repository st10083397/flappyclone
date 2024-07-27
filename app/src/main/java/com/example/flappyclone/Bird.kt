package com.example.flappyclone

import android.graphics.Bitmap
import android.graphics.Canvas

class Bird(private val birdBitmap: Bitmap) {
    private var x: Float = 100f
    private var y: Float = 100f
    private var velocity: Float = 0f
    private val gravity: Float = 1f
    private val flapPower: Float = -15f
    private val birdWidth: Int = birdBitmap.width
    private val birdHeight: Int = birdBitmap.height

    fun update() {
        if (flapped) {
            velocity = flapPower
            flapped = false
        }
        velocity += gravity
        y += velocity
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(birdBitmap, x, y, null)
    }

    companion object {
        var flapped = false
    }
}