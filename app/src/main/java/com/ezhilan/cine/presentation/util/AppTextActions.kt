package com.ezhilan.cine.presentation.util

import com.ezhilan.cine.core.Constants
import java.util.Calendar

class AppTextActions {
    fun convertCalendar(calendar: Calendar): String {
        val date = Constants.DOUBLE_ZERO_PADDING.format(calendar.get(Calendar.DATE))
        val month = Constants.MONTH_LIST[calendar.get(Calendar.MONTH)]
        val year = calendar.get(Calendar.YEAR).toString().takeLast(2)
        return "$date $month $year"
    }
}