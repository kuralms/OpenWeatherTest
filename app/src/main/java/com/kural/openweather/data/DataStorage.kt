package com.kural.openweather.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.appDataStore by preferencesDataStore(name = "app_data")

class AppCache (private val context: Context) {
    companion object {
        val CACHE_CITY = stringPreferencesKey("CACHE_CITY")
    }

    suspend fun setCity(name: String) {
        context.appDataStore.edit {
            it[CACHE_CITY] = name
        }
    }

    val getCity: Flow<String> = context.appDataStore.data.map {
        it[CACHE_CITY] ?: ""
    }
}