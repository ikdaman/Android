package project.side.ikdaman.domain.usecase

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.RecordType
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class UpdateBookLogUseCase @Inject constructor(
    private val bookApiRepository: MyBooksApiRepository
) {
    operator fun invoke(
        bookId: String,
        logId: Int,
        type: String,
        content: String,
    ): Flow<ApiResult<Unit>> {
        return when (type) {
            RecordType.THINK -> {
                bookApiRepository.updateThink(bookId, logId, content)
            }
            RecordType.COMPLETED -> {
                bookApiRepository.updateCompleted(bookId, logId, content)
            }
            else -> {
                throw IllegalArgumentException("Invalid record type: $type")
            }
        }
    }
}