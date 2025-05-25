package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.HomeBookItem

interface BookApiRepository {
    fun getBooks(): Flow<ApiResult<List<HomeBookItem>>>
}