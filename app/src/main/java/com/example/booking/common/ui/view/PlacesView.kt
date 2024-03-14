package com.example.booking.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.example.booking.R
import com.example.booking.services.domain.model.Place

open class PlacesView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    protected val placePaint = Paint().apply {
        color = context.getColor(R.color.black)
        style = Paint.Style.FILL
    }

    private var height: Int = 0
    private var width: Int = 0

    var places: List<Place> = listOf()
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        height = places.flatMap { place ->
//            place.coordinates.map {
//                it.yPosition
//            }
//        }.max()
//        width = places.flatMap { place ->
//            place.coordinates.map {
//                it.xPosition
//            }
//        }.max()
//        setMeasuredDimension(
//            (width + 1) * COORDINATE_SIZE,
//            (width + 1) * COORDINATE_SIZE,
//        )
//    }


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