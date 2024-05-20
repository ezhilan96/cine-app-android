package com.ezhilan.cine.core

const val TAG = "logi"

object Constants {
    const val GOOGLE_DNS = "8.8.8.8"
    const val SUPPORT_CONTACT_NO = "+91 89392 92000"

    const val DEFAULT_STRING: String = ""
    const val DEFAULT_INT: Int = -1
    const val DEFAULT_LONG: Long = -1L
    const val DEFAULT_DOUBLE: Double = -1.0
    const val DEFAULT_FILE_EXTENSION: String = ".jpg"

    val MONTH_LIST: Array<String> =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
//    val DAY_OF_WEEK: Array<String> = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    const val DOUBLE_ZERO_PADDING = "%02d"
    const val SINGLE_DECIMAL_WRAPPING: String = "#.#"
    const val UTC_DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val OTP_REGEX = "[0-9]{4}"
    const val AM = "AM"
    const val PM = "PM"

    const val ROUTE_SPLASH = "splashScreen"
    const val ROUTE_AUTH = "auth"
    const val ROUTE_HOME = "home"

    const val KEY_STRING = "string"
    const val KEY_PLURALS = "plurals"
    const val KEY_USER_PREFERENCES = "userPreferences"

    const val JSON_AUTH_HEADER = "Authorization"
    const val JSON_BEARER_PREFIX = "bearer"

    const val ROUTE_LOGIN = "Route login"
    const val ROUTE_OTP = "Route otp"
    const val ROUTE_DASHBOARD = "Route dashboard"
    const val ROUTE_MOVIE = "Route movie"
    const val ROUTE_TV = "Route tv"
    const val ROUTE_PEOPLE = "Route people"
    const val ROUTE_TRENDING = "Route trending"
    const val ROUTE_DISCOVER = "Route discover"
    const val ROUTE_SETTINGS = "Route settings"

    const val ROUTE_ARG_PHONE = "phone"
}