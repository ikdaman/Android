package project.side.ikdaman.domain.usecase

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.side.ikdaman.domain.repository.AlarmRepository
import javax.inject.Inject

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AlarmReceiverEntryPoint {
    fun reScheduleAlarmUseCase(): ReScheduleAlarmUseCase
}

class ReScheduleAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke(hour: Int, minute: Int) {
        alarmRepository.rescheduleAlarm(hour, minute)
    }
}