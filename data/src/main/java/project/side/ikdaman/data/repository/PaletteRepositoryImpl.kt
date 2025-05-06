package project.side.ikdaman.data.repository

import project.side.ikdaman.data.service.PaletteService
import project.side.ikdaman.domain.repository.PaletteRepository

class PaletteRepositoryImpl(
    private val paletteService: PaletteService
) : PaletteRepository {
    override fun getPalette() = paletteService.currentPalette
    override suspend fun setPalette(color: Long) = paletteService.setPalette(color)
}