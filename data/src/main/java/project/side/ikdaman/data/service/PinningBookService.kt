package project.side.ikdaman.data.service

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pinned_settings")

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
    fun pinItem(itemId: String) = flow {
        context.dataStore.edit { preferences ->
            val currentPinned = preferences[PINNED_ITEMS_KEY] ?: emptySet()
            preferences[PINNED_ITEMS_KEY] = currentPinned + itemId
        }
        emit(true)
    }.catch {
        emit(false)
    }

    // 항목 고정 해제
    fun unpinItem(itemId: String) = flow {
        context.dataStore.edit { preferences ->
            val currentPinned = preferences[PINNED_ITEMS_KEY] ?: emptySet()
            preferences[PINNED_ITEMS_KEY] = currentPinned - itemId
        }
        emit(true)
    }.catch {
        emit(false)
    }
}