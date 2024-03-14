package com.example.booking.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.content.res.AppCompatResources
import com.example.booking.R
import com.example.booking.services.domain.model.Place
import com.example.booking.services.domain.model.PlaceCoordinate
import kotlin.math.ceil


class ScrollablePlacesView(context: Context, attrs: AttributeSet?) : PlacesView(context, attrs) {
    private var scaleFactor = 1.0f

    private var offsetX = 0f
    private var offsetY = 0f
    private var lastX = 0f
    private var lastY = 0f

    private val grayPaint = Paint().apply {
        color = context.getColor(R.color.gray)
        style = Paint.Style.FILL
    }

    private val greenPaint = Paint().apply {
        color = context.getColor(R.color.green)
        style = Paint.Style.FILL
    }

    private val scaleGestureDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, ScaleListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - lastX
                val deltaY = event.y - lastY
                offsetX += deltaX
                offsetY += deltaY
                lastX = event.x
                lastY = event.y
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(offsetX, offsetY)
        canvas.scale(scaleFactor, scaleFactor)

        places.forEach { place ->
            place.coordinates.forEach { coordinate ->
                val left = coordinate.xPosition * COORDINATE_SIZE
                val top = coordinate.yPosition * COORDINATE_SIZE
                val right = (coordinate.xPosition + 1) * COORDINATE_SIZE + 1
                val bottom = (coordinate.yPosition + 1) * COORDINATE_SIZE + 1

                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), place.placeColor())
            }
            val coordinates = findCenterOrTopLeft(place.coordinates)
            val xPlace = coordinates.xPosition
            val yPlace = coordinates.yPosition

            val imageLeft = ceil((xPlace * COORDINATE_SIZE + 1)).toInt()
            val imageRight = ceil((xPlace + 0.5) * COORDINATE_SIZE).toInt()
            val imageTop = ceil((yPlace + 0.25) * COORDINATE_SIZE).toInt()
            val imageBottom = ceil((yPlace + 0.75) * COORDINATE_SIZE).toInt()

            val drawable = AppCompatResources.getDrawable(context, R.drawable.baseline_person_24)
            drawable!!.setBounds(imageLeft, imageTop, imageRight, imageBottom)
            drawable.draw(canvas)

            val textLeft = imageRight.toFloat() + 1
            val textTop = imageBottom.toFloat() - 5
            placePaint.textSize = 20f
            canvas.drawText(place.peopleAmount.toString(), textLeft, textTop, placePaint)
        }
        canvas.restore()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(MIN_SCALE_FACTOR, MAX_SCALE_FACTOR)
            invalidate()
            return true
        }
    }

    private fun Place.placeColor(): Paint {
        return if (this.isFree) {
            greenPaint
        } else {
            grayPaint
        }
    }

    private fun findCenterOrTopLeft(coordinates: List<PlaceCoordinate>): Coordinate {
        if (coordinates.size == 1) {
            return Coordinate(
                coordinates[0].xPosition.toDouble(),
                coordinates[0].yPosition.toDouble()
            )
        }

        val xList = coordinates.map { it.xPosition }
        val yList = coordinates.map { it.yPosition }
        val avgX = xList.average()
        val avgY = yList.average()

        return if (xList.toSet().size == 1 || yList.toSet().size == 1) {
            return Coordinate(avgX, avgY)
        }
        else Coordinate(
            coordinates[0].xPosition.toDouble(),
            coordinates[0].yPosition.toDouble()
        )
    }

    private data class Coordinate(
        val xPosition: Double,
        val yPosition: Double
    )

    companion object {
        private const val MIN_SCALE_FACTOR = 0.4f
        private const val MAX_SCALE_FACTOR = 3.0f
    }
}