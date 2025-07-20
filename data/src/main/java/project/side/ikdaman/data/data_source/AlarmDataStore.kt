package project.side.ikdaman.data.data_source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.AlarmDataStore: DataStore<Preferences> by preferencesDataStore(name = "alarm")

class AlarmDataStore(private val context: Context) {

    companion object {
        val ALARM_TIME_KEY = stringPreferencesKey("alarm_time")
    }

    suspend fun saveAlarmTime(time: String) {
        context.AlarmDataStore.edit { prefs ->
            prefs[ALARM_TIME_KEY] = time
        }
    }

    suspend fun getAlarmTime(): String? {
        return context.AlarmDataStore.data.first()[ALARM_TIME_KEY]
    }

    suspend fun clearAlarmTime() {
        context.AlarmDataStore.edit { prefs ->
            prefs.remove(ALARM_TIME_KEY)
        }
    }
}