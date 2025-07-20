package project.side.ikdaman.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.side.ikdaman.data.data_source.AlarmDataStore
import project.side.ikdaman.domain.repository.AlarmRepository
import project.side.ikdaman.domain.util.ALARM_ACTION
import java.util.Calendar
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val context: Context,
    private val alarmDataStore: AlarmDataStore,
    private val alarmManager: AlarmManager
): AlarmRepository {

    override suspend fun scheduleAlarms(timeString: String) {
        val (hour, minute) = parseTimeString(timeString)

        scheduleAlarmForDay(Calendar.MONDAY, hour, minute, 1)
        scheduleAlarmForDay(Calendar.FRIDAY, hour, minute, 2)

        alarmDataStore.saveAlarmTime(timeString)
    }

    private fun parseTimeString(timeString: String): Pair<Int, Int> {
        val trimmed = timeString.trim().uppercase()
        return if (trimmed.contains("AM") || trimmed.contains("PM")) {
            // 12시간 형식 처리
            val amPm = if (trimmed.contains("PM")) "PM" else "AM"
            val timePart = trimmed.replace("AM", "").replace("PM", "").trim()
            val (hourStr, minuteStr) = timePart.split(":")
            var hour = hourStr.toInt()
            val minute = minuteStr.toInt()
            if (amPm == "PM" && hour != 12) hour += 12
            if (amPm == "AM" && hour == 12) hour = 0
            hour to minute
        } else {
            // 24시간 형식 처리
            val (hour, minute) = trimmed.split(":").map { it.toInt() }
            hour to minute
        }
    }

    private fun scheduleAlarmForDay(dayOfWeek: Int, hour: Int, minute: Int, requestCode: Int) {
        val intent = Intent(ALARM_ACTION)
        intent.setPackage(context.packageName)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // 만약 설정 시간이 현재 시간보다 이전이면 다음 주로 설정
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        Log.i("AlarmRepository", "알람 설정: 요일=$dayOfWeek, 시간=$hour:$minute, 요청 코드=$requestCode, 시간=${calendar.time}")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    override fun cancelAlarms() {
        // 월요일 알람 취소
        cancelAlarmForDay(1)
        // 금요일 알람 취소
        cancelAlarmForDay(2)

        CoroutineScope(Dispatchers.IO).launch {
            alarmDataStore.clearAlarmTime()
        }
    }

    override suspend fun getAlarmTime(): String? {
        return alarmDataStore.getAlarmTime()
    }

    private fun cancelAlarmForDay(requestCode: Int) {
        val intent = Intent(ALARM_ACTION)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}