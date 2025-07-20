package project.side.ikdaman.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.repository.AlarmRepository
import javax.inject.Inject

class ScheduleAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke(timeString: String) {
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.scheduleAlarms(timeString)
        }
    }
}