package project.side.ikdaman.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeUTC {
    fun nowToCustomFormat(): String {
        return SimpleDateFormat(
            "yyyy년 MM월 dd일 HH시 mm분",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
    }
}