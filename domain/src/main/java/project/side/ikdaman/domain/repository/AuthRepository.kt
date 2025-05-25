package project.side.ikdaman.domain.repository

import project.side.ikdaman.domain.model.ApiResult

interface AuthRepository {
    suspend fun login(token: String, provider: String, providerId: String): ApiResult<Unit>
}