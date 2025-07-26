package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.AlarmRepository
import javax.inject.Inject

class GetSavedAlarmTimeUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(): String? {
        return alarmRepository.getAlarmTime()
    }
}