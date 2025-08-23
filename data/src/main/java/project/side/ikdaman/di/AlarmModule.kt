package project.side.ikdaman.di

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import project.side.ikdaman.data.data_source.local.AlarmDataStore
import project.side.ikdaman.data.repository.AlarmRepositoryImpl
import project.side.ikdaman.domain.repository.AlarmRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AlarmManagerQualifier

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    // provide application context
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    @AlarmManagerQualifier
    fun provideAlarmManager(applicationContext: Context): AlarmManager {
        return applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(applicationContext: Context, alarmDataStore: AlarmDataStore, @AlarmManagerQualifier alarmManager: AlarmManager): AlarmRepository {
        return AlarmRepositoryImpl(applicationContext, alarmDataStore, alarmManager)
    }
}