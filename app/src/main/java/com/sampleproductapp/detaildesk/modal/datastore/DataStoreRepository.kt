package com.sampleproductapp.detaildesk.modal.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "user_credentials")

// Repository for preferences access
class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val SESSION_TOKEN = stringPreferencesKey("session_token")
        val  USER_ID = longPreferencesKey("user_id")
    }

    // Retrieve session token
    val sessionToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[SESSION_TOKEN]
    }


    val userId: Flow<Long?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }
    // Save session token
    suspend fun saveSessionToken(token: String, id: Long) {
        dataStore.edit { preferences ->
            preferences[SESSION_TOKEN] = token
            preferences[USER_ID] = id
        }
    }
    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesRepository(
        @ApplicationContext context: Context
    ): DataStoreRepository {
        return DataStoreRepository(context)
    }
}
@Module
@InstallIn(SingletonComponent::class) // Singleton scope
object ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext
}
