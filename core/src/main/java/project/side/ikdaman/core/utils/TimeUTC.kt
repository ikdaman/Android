package project.side.ikdaman.core.utils

import java.text.SimpleDateFormat
import java.util.Locale

object TimeUTC {
    fun now(): String {
        val now = System.currentTimeMillis()
        // UTC format
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(java.util.Date(now))
    }

    fun nowToCustomFormat(): String {
        val now = System.currentTimeMillis()
        // Custom format ex) 2024년 12월 23일 22시 15분
        return SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.getDefault()).format(java.util.Date(now))
    }
}