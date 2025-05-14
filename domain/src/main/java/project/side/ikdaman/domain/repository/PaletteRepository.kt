package project.side.ikdaman.domain.repository

import kotlinx.coroutines.flow.Flow

interface PaletteRepository {
    fun getPalette(): Flow<Long>
    suspend fun setPalette(color: Long)
}