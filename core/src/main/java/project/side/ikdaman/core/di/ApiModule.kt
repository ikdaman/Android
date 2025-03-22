package project.side.ikdaman.core.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.side.ikdaman.core.data.repository.BookRepository
import project.side.ikdaman.core.data.service.BookService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideAladinService(): BookService{
        return Retrofit.Builder()
            .baseUrl("http://www.aladin.co.kr/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BookService::class.java)
    }

    @Provides
    fun provideBookRepository(service: BookService): BookRepository{
        return BookRepository(service)
    }
}