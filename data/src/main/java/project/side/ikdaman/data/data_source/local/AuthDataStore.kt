package project.side.ikdaman.data.data_source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.AuthDatStore: DataStore<Preferences> by preferencesDataStore(name = "auth_pref")

class AuthDataStore(private val context: Context) {
    companion object {
        val PROVIDER_KEY = stringPreferencesKey("provider")
        val AUTHORIZATION_KEY = stringPreferencesKey("Authorization")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh-token")
        val NICKNAME_KEY = stringPreferencesKey("nickname")
    }

    val nickname: Flow<String?> = context.AuthDatStore.data.map { prefs ->
        prefs[NICKNAME_KEY]
    }

    suspend fun getProvider(): String? = context.AuthDatStore.data.first()[PROVIDER_KEY]

    suspend fun getAuthorization(): String? = context.AuthDatStore.data.first()[AUTHORIZATION_KEY]

    suspend fun getRefreshToken(): String? = context.AuthDatStore.data.first()[REFRESH_TOKEN_KEY]

    suspend fun saveToken(
        authorization: String,
        refreshToken: String
    ) {
        context.AuthDatStore.edit { prefs ->
            prefs[AUTHORIZATION_KEY] = authorization
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun saveAuthInfo(
        provider: String,
        authorization: String,
        refreshToken: String,
        nickname: String
    ) {
        context.AuthDatStore.edit { prefs ->
            prefs[PROVIDER_KEY] = provider
            prefs[AUTHORIZATION_KEY] = authorization
            prefs[REFRESH_TOKEN_KEY] = refreshToken
            prefs[NICKNAME_KEY] = nickname
        }
    }

    suspend fun saveNickname(nickname: String) {
        context.AuthDatStore.edit { prefs ->
            prefs[NICKNAME_KEY] = nickname
        }
    }

    suspend fun clear() {
        context.AuthDatStore.edit { it.clear() }
    }
}