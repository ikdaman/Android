package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.AlarmRepository
import javax.inject.Inject

class ScheduleAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke(timeString: String) {
        alarmRepository.scheduleAlarms(timeString)
    }
}