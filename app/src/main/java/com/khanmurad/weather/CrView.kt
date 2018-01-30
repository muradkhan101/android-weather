package com.khanmurad.weather

import android.content.Context
import android.graphics.Canvas
import android.support.annotation.CallSuper
import android.util.AttributeSet
import android.view.View
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

open class CrView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
): View(context, attrs, defStyleAttr, defStyleRes) {
    private var drawer: Canvas.() -> Unit = {}
    private var continuation: Continuation<Unit>? = null
    // What is this canvas syntax?
    fun doOnDraw(drawer: Canvas.() -> Unit) { this.drawer = drawer }
    suspend fun redraw() {
        // What does require mean?
        require(continuation === null) {
            "Redrawing is already in progress"
        }
        invalidate()
        suspendCoroutine<Unit> { continuation = it }
    }
    suspend fun draw(drawer: Canvas.() -> Unit) {
        doOnDraw(drawer)
        redraw()
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        drawer.invoke(canvas)
        // What are post and resume? coroutine stuffs probably
        continuation?.run { post{ resume(Unit)} }
        continuation = null
    }
}