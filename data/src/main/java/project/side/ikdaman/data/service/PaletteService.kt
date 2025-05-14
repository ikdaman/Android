package project.side.ikdaman.data.service

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "palette_settings")

class PaletteService(private val context: Context) {
    companion object {
        val PALETTE_KEY = longPreferencesKey("palette_key")
    }

    // 현재 팔레트 가져오기
    val currentPalette: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[PALETTE_KEY]?.toLong() ?: 0xFFC1A4DB // 기본 팔레트 색상
        }

    // 팔레트 설정
    suspend fun setPalette(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[PALETTE_KEY] = color
        }
    }
}