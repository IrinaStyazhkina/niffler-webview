package ru.niffer_android.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import ru.niffer_android.model.Statistics
import ru.niffer_android.utils.AndroidUtils
import ru.niffer_android.utils.CHART_COLORS
import ru.niffer_android.utils.ColorUtils
import ru.niffer_android.utils.CurrencyUtils
import kotlin.math.min

class StatisticsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attributeSet, defStyleAttr, defStyleRes) {
    private val textSize = AndroidUtils.dp(context, 20).toFloat()
    private val lineWidth = AndroidUtils.dp(context, 16).toFloat()
    private val colors = CHART_COLORS

    private var progress = 0F
    private var valueAnimator: ValueAnimator? = null

    var statistics: Statistics? = null
        set(value) {
            field = value
            update()
        }

    private var radius = 0F
    private var center = PointF()
    private var oval = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = lineWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.MITER
        strokeCap = Paint.Cap.SQUARE
        isAntiAlias = false
        color = Color.parseColor("#A8ACC0")
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = this@StatisticsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius,
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, radius, paint)

        val currency = CurrencyUtils.getCurrencySign(statistics?.currency ?: "RUB")
        val sum = statistics?.total ?: 0.0

        val totalSum = "$sum $currency"
        val categoriesData = statistics?.statByCategories
        var startAngle = -90F

        categoriesData?.forEachIndexed { index, it ->
            val angle = (it.sum * 360 / sum).toFloat()
            paint.color = colors.getOrElse(index) { ColorUtils.randomColor() }
            canvas.drawArc(
                oval,
                startAngle + progress * 360,
                angle * progress,
                false,
                paint
            )
            startAngle += angle
        }

        canvas.drawText(totalSum, center.x, center.y, textPaint)
    }

    private fun update() {

        valueAnimator?.let {
            it.removeAllListeners()
            it.cancel()
        }

        progress = 0F

        valueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener { animation ->
                progress = animation.animatedValue as Float
                invalidate()
            }
            duration = 700
            interpolator = LinearInterpolator()
        }.also {
            it.start()
        }
    }
}