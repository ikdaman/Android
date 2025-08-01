package project.side.ikdaman.domain.usecase

import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

class GetBooksOnShelfUseCase @Inject constructor(private val myBooksApiRepository: MyBooksApiRepository) {
    operator fun invoke(status: String?, keyword: String?, page: Int, limit: Int) =
        myBooksApiRepository.getBooksOnShelf(status, keyword, page, limit)
}