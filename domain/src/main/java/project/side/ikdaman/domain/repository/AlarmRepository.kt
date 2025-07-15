package project.side.ikdaman.domain.repository

interface AlarmRepository {
    fun scheduleAlarms(timeString: String)
    fun cancelAlarms()
    suspend fun getAlarmTime(): String?
}