package ru.niffer_android.utils


import android.graphics.Color
import kotlin.random.Random
val CHART_COLORS = listOf(
    Color.parseColor("#FFB703"),
    Color.parseColor("#35AD7B"),
    Color.parseColor("#2941CC"),
    Color.parseColor("#FB8500"),
    Color.parseColor("#219EBC"),
    Color.parseColor("#F75943"),
    Color.parseColor("#162995"),
    Color.parseColor("#63B5E2"),
    Color.parseColor("#9455C6")
)

object ColorUtils {
    fun randomColor() = Random.nextInt(0xFF0000000.toInt(), 0xFFFFFFFF.toInt())
}