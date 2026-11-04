package com.example.model

enum class AppScreen {
    Splash,
    Onboarding1,
    Onboarding2,
    Onboarding3,
    Home,
    Settings,
    BlackOverlay,
    AODCustomization
}

enum class ClockStyle {
    DIGITAL,
    ANALOG
}

data class AppUiState(
    val currentScreen: AppScreen = AppScreen.Splash,
    val floatingButtonEnabled: Boolean = true,
    val overlayOpacityPercent: Int = 100,
    val alwaysOnDisplayEnabled: Boolean = true,
    val volumeGestureEnabled: Boolean = true,
    val peepProtectionEnabled: Boolean = true,
    val biometricLockEnabled: Boolean = true,
    val clockStyle: ClockStyle = ClockStyle.DIGITAL,
    val selectedClockPreviewIndex: Int = 0, // 0: Digital Cyan, 1: Digital White, 2: Analog
    val showDate: Boolean = true,
    val showBattery: Boolean = true,
    val batterySavedPercent: Int = 12,
    val showScreenJumpSheet: Boolean = false,
    val statusMessage: String? = null,
    val showOverlayPermissionPrompt: Boolean = false,
    val showBiometricUnlockPrompt: Boolean = false,
    val peepTapCount: Int = 0
)
