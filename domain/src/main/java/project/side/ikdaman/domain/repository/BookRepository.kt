package project.side.ikdaman.domain.repository

import project.side.ikdaman.domain.model.BookSearch

interface BookRepository {
    suspend fun searchBookWithTitle(title: String): BookSearch
}