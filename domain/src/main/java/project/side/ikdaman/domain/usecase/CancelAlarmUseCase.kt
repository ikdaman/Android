package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.AlarmRepository
import javax.inject.Inject

class CancelAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke() {
        alarmRepository.cancelAlarms()
    }
}