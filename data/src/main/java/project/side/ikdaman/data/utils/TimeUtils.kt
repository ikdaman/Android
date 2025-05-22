package project.side.ikdaman.data.utils

import java.text.SimpleDateFormat
import java.util.Locale

object TimeUtils {
    fun nowUTC(): String {
        val now = System.currentTimeMillis()
        // UTC format
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(java.util.Date(now))
    }
}