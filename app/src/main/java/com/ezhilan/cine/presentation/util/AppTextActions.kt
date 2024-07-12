package com.ezhilan.cine.presentation.util

import com.ezhilan.cine.core.Constants
import com.ezhilan.cine.core.Constants.SINGLE_DECIMAL_WRAPPING
import java.text.DecimalFormat
import java.util.Calendar
import kotlin.math.roundToInt

class AppTextActions {
    fun convertCalendar(calendar: Calendar): String {
        val date = Constants.DOUBLE_ZERO_PADDING.format(calendar.get(Calendar.DATE))
        val month = Constants.MONTH_LIST[calendar.get(Calendar.MONTH)]
        val year = calendar.get(Calendar.YEAR).toString().takeLast(2)
        return "$date $month $year"
    }

    fun limitDecimalPoint(
        value: Double?,
        decimalWrapping: String = SINGLE_DECIMAL_WRAPPING,
    ): String = (value ?: 0.0).let {
        DecimalFormat(decimalWrapping).format((it * 10.0).roundToInt() / 10.0)
    }
}