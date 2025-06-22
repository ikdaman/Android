package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.model.RecordType
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class DeleteLogUseCase @Inject constructor(
    private val bookApiRepository: MyBooksApiRepository
) {
    operator fun invoke(
        bookId: String,
        logId: Int,
        type: String
    ) = when (type) {
        RecordType.THINK -> bookApiRepository.deleteThink(bookId, logId)
        RecordType.COMPLETED -> bookApiRepository.deleteCompleted(bookId, logId)
        else -> throw IllegalArgumentException("Invalid record type: $type")
    }
}