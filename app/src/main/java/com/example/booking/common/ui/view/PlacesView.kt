package com.example.booking.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.booking.R
import com.example.booking.establishments.domain.model.Place

/**
 * [View], отображающий места в зале
 */
open class PlacesView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val placePaint = Paint().apply {
        color = context.getColor(R.color.md_theme_primary)
        style = Paint.Style.FILL
    }

    /**
     * Список мест в зале
     */
    var places: List<Place> = listOf()
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        places.forEach { place ->
            place.coordinates.forEach { coordinate ->
                val left = coordinate.xPosition * COORDINATE_SIZE
                val top = coordinate.yPosition * COORDINATE_SIZE
                val right = (coordinate.xPosition + 1) * COORDINATE_SIZE
                val bottom = (coordinate.yPosition + 1) * COORDINATE_SIZE
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), placePaint)
            }
        }
    }

    companion object {
        const val COORDINATE_SIZE = 50
    }
}