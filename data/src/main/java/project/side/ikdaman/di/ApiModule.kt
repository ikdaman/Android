package project.side.ikdaman.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.side.ikdaman.data.repository.BookRepositoryImpl
import project.side.ikdaman.data.service.BookService
import project.side.ikdaman.domain.repository.BookRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideAladinService(): BookService {
        return Retrofit.Builder()
            .baseUrl("https://www.aladin.co.kr/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BookService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(aladinService: BookService): BookRepository {
        return BookRepositoryImpl(aladinService)
    }
}