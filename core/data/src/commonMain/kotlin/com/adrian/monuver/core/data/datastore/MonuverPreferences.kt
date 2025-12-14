package com.adrian.monuver.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.adrian.monuver.core.domain.common.ThemeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MonuverPreferences(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification")
        private val REMINDER_DAYS_BEFORE_DUE_KEY = intPreferencesKey("reminder_days_before_due")
        private val REMINDER_AFTER_DUE_DAY_KEY = booleanPreferencesKey("reminder_after_due_day")
        private val REMINDER_FOR_DUE_BILL_KEY = booleanPreferencesKey("reminder_for_due_bill")
        private val THEME_KEY = stringPreferencesKey("theme_setting")
        private val AUTHENTICATION_KEY = booleanPreferencesKey("authentication")
    }

    val isFirstTime: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[FIRST_TIME_KEY] ?: true
        }

    val isNotificationEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[NOTIFICATION_KEY] ?: false
        }

    val reminderDaysBeforeDue: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[REMINDER_DAYS_BEFORE_DUE_KEY] ?: 1
        }

    val isReminderBeforeDueDayEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[REMINDER_AFTER_DUE_DAY_KEY] ?: false
        }

    val isReminderForDueBillEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[REMINDER_FOR_DUE_BILL_KEY] ?: false
        }

    val themeState: Flow<ThemeState> = dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY]) {
                ThemeState.Light.toString() -> ThemeState.Light
                ThemeState.Dark.toString() -> ThemeState.Dark
                else -> ThemeState.System
            }
        }

    val isAuthenticationEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[AUTHENTICATION_KEY] ?: false
        }

    suspend fun setFirstTimeToFalse() {
        dataStore.edit { preferences ->
            preferences[FIRST_TIME_KEY] = false
        }
    }

    suspend fun setNotification(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = isEnabled
        }
    }

    suspend fun setReminderSettings(
        reminderDaysBeforeDue: Int,
        isReminderBeforeDueDayEnabled: Boolean,
        isReminderForDueBillEnabled: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[REMINDER_DAYS_BEFORE_DUE_KEY] = reminderDaysBeforeDue
            preferences[REMINDER_AFTER_DUE_DAY_KEY] = isReminderBeforeDueDayEnabled
            preferences[REMINDER_FOR_DUE_BILL_KEY] = isReminderForDueBillEnabled
        }
    }

    suspend fun setThemeState(themeState: ThemeState) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeState.toString()
        }
    }

    suspend fun setAuthentication(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTHENTICATION_KEY] = isEnabled
        }
    }
}