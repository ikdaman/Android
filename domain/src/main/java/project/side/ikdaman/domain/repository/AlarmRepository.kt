package project.side.ikdaman.domain.repository

interface AlarmRepository {
    suspend fun scheduleAlarms(timeString: String)
    fun cancelAlarms()
    suspend fun getAlarmTime(): String?
}