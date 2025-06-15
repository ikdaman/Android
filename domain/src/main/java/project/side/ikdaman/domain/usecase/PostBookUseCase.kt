package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.repository.BookApiRepository
import javax.inject.Inject

class PostBookUseCase @Inject constructor(private val bookApiRepository: BookApiRepository) {
    operator fun invoke(addBookItem: AddBookItem) = bookApiRepository.postBook(addBookItem)
}