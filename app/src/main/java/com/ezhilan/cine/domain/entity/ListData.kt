package com.ezhilan.cine.domain.entity

import com.ezhilan.cine.core.Constants

enum class BookingType { customer, taxida, business, ets }

data class ListData(
    val id: Int = Constants.DEFAULT_INT,
    val date: String = Constants.DEFAULT_STRING,
    val time: String = Constants.DEFAULT_STRING,
)