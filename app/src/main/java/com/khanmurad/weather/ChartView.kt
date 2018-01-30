package com.khanmurad.weather

import android.content.Context
import android.util.AttributeSet
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay

class ChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
): CrView(context, attrs, defStyleAttr, defStyleRes) {

    var chart = Chart(0f..100f, 0f..100f, emptyList())
        set(value) {
            field = value
            actor.offer(value)
        }
    private val actor = actor<Chart>(UI, Channel.CONFLATED) {
        var currentChart = chart.deepCopy()
        var currentVelocities = chart.deepCopy().resetPoints()

        doOnDraw { drawChart(currentChart) }

        // What does `this` reference in actor? Mailbox?
        for (destinationChart in this) {
            currentChart = currentChart.copyAndReformat(destinationChart, destinationChart.pointAtTheEnd)
            currentVelocities = currentVelocities.copyAndReformat(destinationChart, Point(0f, 0f))

            while (isActive && isEmpty) {
                currentChart.moveABitTo(destinationChart, currentVelocities)
                redraw()
                delay(16)
            }
        }
    }
}