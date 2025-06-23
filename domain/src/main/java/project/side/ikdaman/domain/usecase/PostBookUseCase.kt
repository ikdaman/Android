package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class PostBookUseCase @Inject constructor(private val myBooksApiRepository: MyBooksApiRepository) {
    operator fun invoke(addBookItem: AddBookItem) = myBooksApiRepository.postBook(addBookItem)
}