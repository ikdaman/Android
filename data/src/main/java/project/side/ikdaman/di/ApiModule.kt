package project.side.ikdaman.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.side.ikdaman.data.repository.BookApiRepositoryImpl
import project.side.ikdaman.data.repository.BookRepositoryImpl
import project.side.ikdaman.data.service.BookApiService
import project.side.ikdaman.data.service.BookService
import project.side.ikdaman.domain.repository.BookApiRepository
import project.side.ikdaman.domain.repository.BookRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val API_URL = "https://403f085d-bd13-42ee-a481-11de8752476f.mock.pstmn.io/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

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

    @Provides
    @Singleton
    fun provideBookApiService(okHttpClient: OkHttpClient): BookApiService {
        return Retrofit.Builder()
            .baseUrl(API_URL) // Replace with your actual base URL
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BookApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookApiRepository(bookApiService: BookApiService): BookApiRepository {
        return BookApiRepositoryImpl(bookApiService)
    }
}