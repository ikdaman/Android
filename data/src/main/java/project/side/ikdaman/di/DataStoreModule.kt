package project.side.ikdaman.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.side.ikdaman.data.repository.PaletteRepositoryImpl
import project.side.ikdaman.data.repository.PinningBookRepositoryImpl
import project.side.ikdaman.data.service.PaletteService
import project.side.ikdaman.data.service.PinningBookService
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePinningBookService(application: Application) = PinningBookService(application)

    @Provides
    @Singleton
    fun providePinningBookRepository(pinningBookService: PinningBookService): PinningBookRepository =
        PinningBookRepositoryImpl(pinningBookService)

    @Provides
    @Singleton
    fun providePaletteService(application: Application) = PaletteService(application)

    @Provides
    @Singleton
    fun providePaletteRepository(paletteService: PaletteService): PaletteRepository =
        PaletteRepositoryImpl(paletteService)
}