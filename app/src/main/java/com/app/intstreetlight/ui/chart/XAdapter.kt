package com.app.intstreetlight.ui.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class XAdapter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        val formatter = SimpleDateFormat("HH:mm", Locale.CHINA)
        val str = formatter.format(date)
        return str
    }

}