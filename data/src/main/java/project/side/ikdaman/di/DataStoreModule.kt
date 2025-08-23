package project.side.ikdaman.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.side.ikdaman.data.data_source.local.AlarmDataStore
import project.side.ikdaman.data.repository.PaletteRepositoryImpl
import project.side.ikdaman.data.repository.PinningBookRepositoryImpl
import project.side.ikdaman.data.data_source.local.PaletteDataStore
import project.side.ikdaman.data.data_source.local.AuthDataStore
import project.side.ikdaman.data.data_source.local.PinningBookDataStore
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePinningBookService(application: Application) = PinningBookDataStore(application)

    @Provides
    @Singleton
    fun providePinningBookRepository(pinningBookDataStore: PinningBookDataStore): PinningBookRepository =
        PinningBookRepositoryImpl(pinningBookDataStore)

    @Provides
    @Singleton
    fun providePaletteService(application: Application) = PaletteDataStore(application)

    @Provides
    @Singleton
    fun providePaletteRepository(paletteService: PaletteDataStore): PaletteRepository =
        PaletteRepositoryImpl(paletteService)

	@Provides
	@Singleton
    fun provideAuthDataStore(application: Application) = AuthDataStore(application)

    @Provides
    @Singleton
    fun provideAlarmDataStore(application: Application) = AlarmDataStore(application)
}