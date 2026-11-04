package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SettingsRepository
import com.example.model.AppScreen
import com.example.model.AppUiState
import com.example.model.ClockStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private val backStack = ArrayDeque<AppScreen>()

    init {
        viewModelScope.launch {
            repository.settingsFlow.collect { saved ->
                _uiState.update { current ->
                    current.copy(
                        floatingButtonEnabled = saved.floatingButtonEnabled,
                        overlayOpacityPercent = saved.overlayOpacityPercent,
                        alwaysOnDisplayEnabled = saved.alwaysOnDisplayEnabled,
                        volumeGestureEnabled = saved.volumeGestureEnabled,
                        peepProtectionEnabled = saved.peepProtectionEnabled,
                        biometricLockEnabled = saved.biometricLockEnabled,
                        clockStyle = saved.clockStyle,
                        selectedClockPreviewIndex = saved.selectedClockPreviewIndex,
                        showDate = saved.showDate,
                        showBattery = saved.showBattery,
                        batterySavedPercent = saved.batterySavedPercent
                    )
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen, addToBackStack: Boolean = true) {
        val current = _uiState.value.currentScreen
        if (current == screen) return
        if (addToBackStack && current != AppScreen.Splash) {
            backStack.addLast(current)
        }
        _uiState.update {
            it.copy(
                currentScreen = screen,
                showScreenJumpSheet = false,
                peepTapCount = 0
            )
        }
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            val previous = backStack.removeLast()
            _uiState.update {
                it.copy(
                    currentScreen = previous,
                    showScreenJumpSheet = false,
                    peepTapCount = 0
                )
            }
            return true
        }
        val current = _uiState.value.currentScreen
        return when (current) {
            AppScreen.Settings,
            AppScreen.AODCustomization,
            AppScreen.BlackOverlay -> {
                _uiState.update { it.copy(currentScreen = AppScreen.Home, peepTapCount = 0) }
                true
            }
            AppScreen.Onboarding3 -> {
                _uiState.update { it.copy(currentScreen = AppScreen.Onboarding2) }
                true
            }
            AppScreen.Onboarding2 -> {
                _uiState.update { it.copy(currentScreen = AppScreen.Onboarding1) }
                true
            }
            else -> false
        }
    }

    fun onSplashFinished() {
        if (_uiState.value.currentScreen == AppScreen.Splash) {
            _uiState.update { it.copy(currentScreen = AppScreen.Onboarding1) }
        }
    }

    fun onSkipOnboarding() {
        backStack.clear()
        _uiState.update { it.copy(currentScreen = AppScreen.Home) }
    }

    fun onNextOnboarding() {
        when (_uiState.value.currentScreen) {
            AppScreen.Onboarding1 -> navigateTo(AppScreen.Onboarding2, addToBackStack = true)
            AppScreen.Onboarding2 -> navigateTo(AppScreen.Onboarding3, addToBackStack = true)
            AppScreen.Onboarding3 -> {
                backStack.clear()
                _uiState.update { it.copy(currentScreen = AppScreen.Home) }
            }
            else -> Unit
        }
    }

    fun activateBlackScreen() {
        navigateTo(AppScreen.BlackOverlay, addToBackStack = true)
    }

    fun exitBlackScreen() {
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.Home,
                peepTapCount = 0,
                showBiometricUnlockPrompt = false
            )
        }
        backStack.removeAll { it == AppScreen.BlackOverlay }
    }

    fun onBlackOverlayTapToUnlock() {
        val state = _uiState.value
        if (state.peepProtectionEnabled && state.peepTapCount < 1) {
            // First tap arms unlock when Peep Protection is active; second tap or floating button unlocks
            _uiState.update { it.copy(peepTapCount = it.peepTapCount + 1) }
        } else {
            exitBlackScreen()
        }
    }

    fun onVolumeKeyToggleOverlay(): Boolean {
        val state = _uiState.value
        if (!state.volumeGestureEnabled) return false
        if (state.currentScreen == AppScreen.BlackOverlay) {
            exitBlackScreen()
        } else {
            activateBlackScreen()
        }
        return true
    }

    fun toggleScreenJumpSheet(show: Boolean) {
        _uiState.update { it.copy(showScreenJumpSheet = show) }
    }

    fun setFloatingButtonEnabled(enabled: Boolean) {
        _uiState.update { it.copy(floatingButtonEnabled = enabled) }
        viewModelScope.launch { repository.setFloatingButton(enabled) }
    }

    fun setOverlayOpacity(percent: Int) {
        val clamped = percent.coerceIn(20, 100)
        _uiState.update { it.copy(overlayOpacityPercent = clamped) }
        viewModelScope.launch { repository.setOverlayOpacity(clamped) }
    }

    fun setAlwaysOnDisplayEnabled(enabled: Boolean) {
        _uiState.update { it.copy(alwaysOnDisplayEnabled = enabled) }
        viewModelScope.launch { repository.setAlwaysOnDisplay(enabled) }
    }

    fun setVolumeGestureEnabled(enabled: Boolean) {
        _uiState.update { it.copy(volumeGestureEnabled = enabled) }
        viewModelScope.launch { repository.setVolumeGesture(enabled) }
    }

    fun setPeepProtectionEnabled(enabled: Boolean) {
        _uiState.update { it.copy(peepProtectionEnabled = enabled) }
        viewModelScope.launch { repository.setPeepProtection(enabled) }
    }

    fun setBiometricLockEnabled(enabled: Boolean) {
        _uiState.update { it.copy(biometricLockEnabled = enabled) }
        viewModelScope.launch { repository.setBiometricLock(enabled) }
    }

    fun setClockStyle(style: ClockStyle) {
        val newIndex = when (style) {
            ClockStyle.DIGITAL -> if (_uiState.value.selectedClockPreviewIndex == 2) 0 else _uiState.value.selectedClockPreviewIndex
            ClockStyle.ANALOG -> 2
        }
        _uiState.update {
            it.copy(clockStyle = style, selectedClockPreviewIndex = newIndex)
        }
        viewModelScope.launch {
            repository.setClockStyle(style)
            repository.setSelectedClockPreview(newIndex)
        }
    }

    fun setSelectedClockPreview(index: Int) {
        val style = if (index == 2) ClockStyle.ANALOG else ClockStyle.DIGITAL
        _uiState.update {
            it.copy(selectedClockPreviewIndex = index, clockStyle = style)
        }
        viewModelScope.launch {
            repository.setSelectedClockPreview(index)
            repository.setClockStyle(style)
        }
    }

    fun setShowDate(show: Boolean) {
        _uiState.update { it.copy(showDate = show) }
        viewModelScope.launch { repository.setShowDate(show) }
    }

    fun setShowBattery(show: Boolean) {
        _uiState.update { it.copy(showBattery = show) }
        viewModelScope.launch { repository.setShowBattery(show) }
    }

    fun applyAodConfiguration() {
        val s = _uiState.value
        viewModelScope.launch {
            repository.saveAodConfig(
                style = s.clockStyle,
                previewIndex = s.selectedClockPreviewIndex,
                showDate = s.showDate,
                showBattery = s.showBattery
            )
        }
        showStatusMessage("AOD Customization Applied")
        navigateBack()
    }

    fun showStatusMessage(message: String?) {
        _uiState.update { it.copy(statusMessage = message) }
    }

    fun setShowOverlayPermissionPrompt(show: Boolean) {
        _uiState.update { it.copy(showOverlayPermissionPrompt = show) }
    }
}
