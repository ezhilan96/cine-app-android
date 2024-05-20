package com.ezhilan.cine.data.dataSource.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ezhilan.cine.presentation.config.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val userPreferencesDataStore: DataStore<Preferences>
) {
    private val loginStatusKey = booleanPreferencesKey("isLoggedIn")
    private val appThemeKey = stringPreferencesKey("appTheme")

    val isLoggedIn: Flow<Boolean> = userPreferencesDataStore.data.map { preferences ->
        preferences[loginStatusKey] ?: true
    }

    suspend fun login() {
        userPreferencesDataStore.edit {
            it[loginStatusKey] = true
        }
    }

    fun logout() {
        runBlocking {
            userPreferencesDataStore.edit {
                it.clear()
            }
        }
    }

    val appTheme: Flow<AppTheme> = userPreferencesDataStore.data.map { preferences ->
        preferences[appThemeKey]?.let {
            when (it) {
                AppTheme.Light.name -> AppTheme.Light
                AppTheme.Dark.name -> AppTheme.Dark
                else -> AppTheme.System
            }
        } ?: AppTheme.Light
    }

    suspend fun setAppTheme(appTheme: AppTheme) {
        userPreferencesDataStore.edit {
            it[appThemeKey] = appTheme.name
        }
    }
}