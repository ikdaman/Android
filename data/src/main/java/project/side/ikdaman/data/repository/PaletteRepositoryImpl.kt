package project.side.ikdaman.data.repository

import project.side.ikdaman.data.data_source.local.PaletteDataStore
import project.side.ikdaman.domain.repository.PaletteRepository

class PaletteRepositoryImpl(
    private val paletteService: PaletteDataStore
) : PaletteRepository {
    override fun getPalette() = paletteService.currentPalette
    override suspend fun setPalette(color: Long) = paletteService.setPalette(color)
}