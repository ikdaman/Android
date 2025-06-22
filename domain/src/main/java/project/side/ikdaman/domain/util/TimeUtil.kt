package project.side.ikdaman.domain.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun convertUtcToLocalString(utcTime: String): String {
    val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    utcFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = utcFormat.parse(utcTime) ?: return ""

    val localFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    localFormat.timeZone = TimeZone.getDefault()

    return localFormat.format(date)
}

fun convertUtcToLocalLong(utcTime: String): Long {
    val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    utcFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = utcFormat.parse(utcTime) ?: return System.currentTimeMillis()

    return date.time
}