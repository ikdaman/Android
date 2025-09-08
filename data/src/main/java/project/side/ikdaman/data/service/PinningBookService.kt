package project.side.ikdaman.data.service

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pinned_settings")

class PinningBookService(private val context: Context) {

    companion object {
        val PINNED_ITEMS_KEY = stringSetPreferencesKey("pinned_items")
    }

    // 고정된 항목 ID 목록 가져오기
    val pinnedItems: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[PINNED_ITEMS_KEY] ?: emptySet()
        }

    // 항목 고정
    suspend fun pinItem(itemId: String): Boolean {
        try {
            context.dataStore.edit { preferences ->
                val currentPinned = preferences[PINNED_ITEMS_KEY] ?: emptySet()
                preferences[PINNED_ITEMS_KEY] = currentPinned + itemId
            }
            return true
        } catch (e: Exception) {
            Log.e("PinningBookService", "pinItem: $e")
            return false
        }
    }

    // 항목 고정 해제
    suspend fun unpinItem(itemId: String): Boolean {
        try {
            context.dataStore.edit { preferences ->
                val currentPinned = preferences[PINNED_ITEMS_KEY] ?: emptySet()
                preferences[PINNED_ITEMS_KEY] = currentPinned - itemId
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun clearAndAddAll(bookIds: Set<String>): Boolean {

        try {
            context.dataStore.edit { preferences ->
                preferences[PINNED_ITEMS_KEY] = bookIds
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }
}