package com.example.flappyclone

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private val thread: GameThread
    private val backgroundBitmap: Bitmap
    private val birdBitmap: Bitmap
    private val pipeBitmap: Bitmap
    private val bird: Bird
    private val pipes: MutableList<Pipe> = mutableListOf()

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)

        // Load bitmaps from resources
        backgroundBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background)
        birdBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bird)
        pipeBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pipe)

        bird = Bird(birdBitmap)

        // Add initial pipes
        pipes.add(Pipe(context, width, height, pipeBitmap))
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread.setRunning(false)
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        bird.update()
        // Update pipes and check for off-screen
        val iterator = pipes.iterator()
        while (iterator.hasNext()) {
            val pipe = iterator.next()
            pipe.update()
            if (pipe.isOffScreen()) {
                iterator.remove()
            }
        }
        // Add new pipe if needed
        if (pipes.isEmpty() || pipes.last().x < width - 400) {
            pipes.add(Pipe(context, width, height, pipeBitmap))
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null) // Draw background

        bird.draw(canvas)

        // Draw pipes
        for (pipe in pipes) {
            pipe.draw(canvas)
        }
    }
}

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
    private var running: Boolean = false

    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {
        while (running) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()
            if (canvas != null) {
                synchronized(surfaceHolder) {
                    gameView.update()
                    gameView.draw(canvas)
                }
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }
}