package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppUiState
import com.example.model.ClockStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "black_screen_overlay_prefs")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val FLOATING_BUTTON = booleanPreferencesKey("floating_button")
        val OVERLAY_OPACITY = intPreferencesKey("overlay_opacity")
        val ALWAYS_ON_DISPLAY = booleanPreferencesKey("always_on_display")
        val VOLUME_GESTURE = booleanPreferencesKey("volume_gesture")
        val PEEP_PROTECTION = booleanPreferencesKey("peep_protection")
        val BIOMETRIC_LOCK = booleanPreferencesKey("biometric_lock")
        val CLOCK_STYLE = stringPreferencesKey("clock_style")
        val SELECTED_CLOCK_PREVIEW = intPreferencesKey("selected_clock_preview")
        val SHOW_DATE = booleanPreferencesKey("show_date")
        val SHOW_BATTERY = booleanPreferencesKey("show_battery")
        val BATTERY_SAVED = intPreferencesKey("battery_saved")
    }

    val settingsFlow: Flow<AppUiState> = context.dataStore.data.map { prefs ->
        val styleStr = prefs[Keys.CLOCK_STYLE] ?: ClockStyle.DIGITAL.name
        val style = runCatching { ClockStyle.valueOf(styleStr) }.getOrDefault(ClockStyle.DIGITAL)
        AppUiState(
            floatingButtonEnabled = prefs[Keys.FLOATING_BUTTON] ?: true,
            overlayOpacityPercent = prefs[Keys.OVERLAY_OPACITY] ?: 100,
            alwaysOnDisplayEnabled = prefs[Keys.ALWAYS_ON_DISPLAY] ?: true,
            volumeGestureEnabled = prefs[Keys.VOLUME_GESTURE] ?: true,
            peepProtectionEnabled = prefs[Keys.PEEP_PROTECTION] ?: true,
            biometricLockEnabled = prefs[Keys.BIOMETRIC_LOCK] ?: true,
            clockStyle = style,
            selectedClockPreviewIndex = prefs[Keys.SELECTED_CLOCK_PREVIEW] ?: 0,
            showDate = prefs[Keys.SHOW_DATE] ?: true,
            showBattery = prefs[Keys.SHOW_BATTERY] ?: true,
            batterySavedPercent = prefs[Keys.BATTERY_SAVED] ?: 12
        )
    }

    suspend fun setFloatingButton(enabled: Boolean) {
        context.dataStore.edit { it[Keys.FLOATING_BUTTON] = enabled }
    }

    suspend fun setOverlayOpacity(percent: Int) {
        context.dataStore.edit { it[Keys.OVERLAY_OPACITY] = percent.coerceIn(20, 100) }
    }

    suspend fun setAlwaysOnDisplay(enabled: Boolean) {
        context.dataStore.edit { it[Keys.ALWAYS_ON_DISPLAY] = enabled }
    }

    suspend fun setVolumeGesture(enabled: Boolean) {
        context.dataStore.edit { it[Keys.VOLUME_GESTURE] = enabled }
    }

    suspend fun setPeepProtection(enabled: Boolean) {
        context.dataStore.edit { it[Keys.PEEP_PROTECTION] = enabled }
    }

    suspend fun setBiometricLock(enabled: Boolean) {
        context.dataStore.edit { it[Keys.BIOMETRIC_LOCK] = enabled }
    }

    suspend fun setClockStyle(style: ClockStyle) {
        context.dataStore.edit { it[Keys.CLOCK_STYLE] = style.name }
    }

    suspend fun setSelectedClockPreview(index: Int) {
        context.dataStore.edit { it[Keys.SELECTED_CLOCK_PREVIEW] = index.coerceIn(0, 2) }
    }

    suspend fun setShowDate(show: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_DATE] = show }
    }

    suspend fun setShowBattery(show: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_BATTERY] = show }
    }

    suspend fun saveAodConfig(
        style: ClockStyle,
        previewIndex: Int,
        showDate: Boolean,
        showBattery: Boolean
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CLOCK_STYLE] = style.name
            prefs[Keys.SELECTED_CLOCK_PREVIEW] = previewIndex.coerceIn(0, 2)
            prefs[Keys.SHOW_DATE] = showDate
            prefs[Keys.SHOW_BATTERY] = showBattery
        }
    }
}
