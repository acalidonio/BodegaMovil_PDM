package com.acalidonio.bodegamovil.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acalidonio.bodegamovil.model.User
import com.acalidonio.bodegamovil.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenRepositoryImpl(private val context: Context) : TokenRepository {

    companion object {
        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_INITIALS_KEY = stringPreferencesKey("user_initials")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val USER_PROFILE_IMAGE_KEY = stringPreferencesKey("user_profile_image")
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[JWT_TOKEN_KEY] = token
        }
    }

    override suspend fun saveUserDetails(
        id: String,
        name: String,
        initials: String,
        role: String,
        profileImageUrl: String?
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
            preferences[USER_NAME_KEY] = name
            preferences[USER_INITIALS_KEY] = initials
            preferences[USER_ROLE_KEY] = role
            preferences[USER_PROFILE_IMAGE_KEY] = profileImageUrl ?: ""
        }
    }

    override suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_NAME_KEY)
            preferences.remove(USER_INITIALS_KEY)
            preferences.remove(USER_ROLE_KEY)
            preferences.remove(USER_PROFILE_IMAGE_KEY)
        }
    }

    override fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY]
        }
    }

    override fun getUserDetails(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val id = preferences[USER_ID_KEY]
            val name = preferences[USER_NAME_KEY]
            val initials = preferences[USER_INITIALS_KEY]
            val role = preferences[USER_ROLE_KEY]
            val profileImageUrl = preferences[USER_PROFILE_IMAGE_KEY]

            if (id != null && name != null && initials != null && role != null) {
                User(
                    employeeId = id,
                    name = name,
                    initials = initials,
                    role = role,
                    profileImageUrl = profileImageUrl
                )
            } else {
                null
            }
        }
    }
}
