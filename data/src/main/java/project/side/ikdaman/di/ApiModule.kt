package project.side.ikdaman.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import project.side.ikdaman.app.data.BuildConfig
import project.side.ikdaman.data.data_source.AuthDataStore
import project.side.ikdaman.data.repository.AuthRepositoryImpl
import project.side.ikdaman.data.repository.BookRepositoryImpl
import project.side.ikdaman.data.service.AuthService
import project.side.ikdaman.data.service.BookService
import project.side.ikdaman.domain.repository.AuthRepository
import project.side.ikdaman.domain.repository.BookRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provideOkHttpClient(authDataStore: AuthDataStore): OkHttpClient =
        OkHttpClient.Builder().addInterceptor { chain ->
            val authorization = runBlocking {   // TODO runBlocking 수정 필요
                authDataStore.getAuthorization()
            }
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $authorization")
                .build()
            chain.proceed(request)
        }.build()

    @Provides
    @Singleton
    @DefaultRetrofit
    fun provideDefaultRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        moshi: Moshi,
        @AuthOkHttpClient authOkHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(authOkHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideAladinService(moshi: Moshi): BookService {
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
    fun provideAuthService(@DefaultRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        authDataStore: AuthDataStore,
    ): AuthRepository {
        return AuthRepositoryImpl(authService, authDataStore)
    }
}