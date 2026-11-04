package com.example

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppScreen
import com.example.service.BlackScreenTileService
import com.example.service.FloatingOverlayService
import com.example.ui.screens.AODCustomizationScreen
import com.example.ui.screens.BlackOverlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.BlackScreenOverlayTheme
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.PrimaryCardBg
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK)
        )
        handleLaunchIntent(intent)

        setContent {
            BlackScreenOverlayTheme {
                BlackScreenOverlayApp(
                    viewModel = viewModel,
                    onUpdateOverlayWindowState = { isOverlayActive ->
                        updateWindowForBlackOverlay(isOverlayActive)
                    },
                    onRequestSystemOverlayPermission = {
                        requestOverlayPermission()
                    },
                    onStartOrStopFloatingService = { enabled ->
                        syncFloatingOverlayService(enabled)
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleLaunchIntent(intent)
    }

    private fun handleLaunchIntent(intent: Intent?) {
        if (intent?.action == BlackScreenTileService.ACTION_ACTIVATE_BLACK_SCREEN) {
            viewModel.activateBlackScreen()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (viewModel.onVolumeKeyToggleOverlay()) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun updateWindowForBlackOverlay(active: Boolean) {
        val params = window.attributes
        if (active) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            params.screenBrightness = 0.01f
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        }
        window.attributes = params
    }

    private fun syncFloatingOverlayService(enabled: Boolean) {
        if (!Settings.canDrawOverlays(this)) return
        val serviceIntent = Intent(this, FloatingOverlayService::class.java)
        if (enabled) {
            runCatching {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }
            }
        } else {
            stopService(serviceIntent)
        }
    }

    private fun requestOverlayPermission() {
        runCatching {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }
}

@Composable
fun BlackScreenOverlayApp(
    viewModel: MainViewModel,
    onUpdateOverlayWindowState: (Boolean) -> Unit = {},
    onRequestSystemOverlayPermission: () -> Unit = {},
    onStartOrStopFloatingService: (Boolean) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.currentScreen) {
        onUpdateOverlayWindowState(uiState.currentScreen == AppScreen.BlackOverlay)
    }

    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.showStatusMessage(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        Crossfade(
            targetState = uiState.currentScreen,
            animationSpec = tween(durationMillis = 200),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.Splash -> {
                    SplashScreen(
                        onFinished = { viewModel.onSplashFinished() }
                    )
                }

                AppScreen.Onboarding1 -> {
                    OnboardingScreen(
                        pageIndex = 0,
                        onSkip = { viewModel.onSkipOnboarding() },
                        onNext = { viewModel.onNextOnboarding() },
                        onBack = { viewModel.navigateBack() },
                        onJumpToPage = { idx ->
                            val target = when (idx) {
                                0 -> AppScreen.Onboarding1
                                1 -> AppScreen.Onboarding2
                                else -> AppScreen.Onboarding3
                            }
                            viewModel.navigateTo(target)
                        }
                    )
                }

                AppScreen.Onboarding2 -> {
                    OnboardingScreen(
                        pageIndex = 1,
                        onSkip = { viewModel.onSkipOnboarding() },
                        onNext = { viewModel.onNextOnboarding() },
                        onBack = { viewModel.navigateBack() },
                        onJumpToPage = { idx ->
                            val target = when (idx) {
                                0 -> AppScreen.Onboarding1
                                1 -> AppScreen.Onboarding2
                                else -> AppScreen.Onboarding3
                            }
                            viewModel.navigateTo(target)
                        }
                    )
                }

                AppScreen.Onboarding3 -> {
                    OnboardingScreen(
                        pageIndex = 2,
                        onSkip = { viewModel.onSkipOnboarding() },
                        onNext = { viewModel.onNextOnboarding() },
                        onBack = { viewModel.navigateBack() },
                        onJumpToPage = { idx ->
                            val target = when (idx) {
                                0 -> AppScreen.Onboarding1
                                1 -> AppScreen.Onboarding2
                                else -> AppScreen.Onboarding3
                            }
                            viewModel.navigateTo(target)
                        }
                    )
                }

                AppScreen.Home -> {
                    HomeScreen(
                        batterySavedPercent = uiState.batterySavedPercent,
                        floatingButtonEnabled = uiState.floatingButtonEnabled,
                        showScreenJumpSheet = uiState.showScreenJumpSheet,
                        onMenuClick = { viewModel.toggleScreenJumpSheet(true) },
                        onDismissScreenJumpSheet = { viewModel.toggleScreenJumpSheet(false) },
                        onJumpToScreen = { target -> viewModel.navigateTo(target) },
                        onSettingsClick = { viewModel.navigateTo(AppScreen.Settings) },
                        onActivateBlackScreen = { viewModel.activateBlackScreen() },
                        onFloatingButtonShortcutClick = {
                            if (!Settings.canDrawOverlays(context)) {
                                viewModel.setShowOverlayPermissionPrompt(true)
                            } else {
                                val nextState = !uiState.floatingButtonEnabled
                                viewModel.setFloatingButtonEnabled(nextState)
                                onStartOrStopFloatingService(nextState)
                                viewModel.showStatusMessage(
                                    if (nextState) "Floating Button Enabled" else "Floating Button Disabled"
                                )
                            }
                        },
                        onAodShortcutClick = {
                            viewModel.navigateTo(AppScreen.AODCustomization)
                        },
                        onQuickTileShortcutClick = {
                            BlackScreenTileService.requestAddQuickTile(context) { message ->
                                viewModel.showStatusMessage(message)
                            }
                        },
                        onPromoCardClick = {
                            viewModel.navigateTo(AppScreen.Settings)
                        }
                    )
                }

                AppScreen.Settings -> {
                    SettingsScreen(
                        floatingButtonEnabled = uiState.floatingButtonEnabled,
                        overlayOpacityPercent = uiState.overlayOpacityPercent,
                        alwaysOnDisplayEnabled = uiState.alwaysOnDisplayEnabled,
                        volumeGestureEnabled = uiState.volumeGestureEnabled,
                        peepProtectionEnabled = uiState.peepProtectionEnabled,
                        biometricLockEnabled = uiState.biometricLockEnabled,
                        onFloatingButtonChanged = { enabled ->
                            viewModel.setFloatingButtonEnabled(enabled)
                            onStartOrStopFloatingService(enabled)
                        },
                        onOverlayOpacityChanged = { percent ->
                            viewModel.setOverlayOpacity(percent)
                        },
                        onAlwaysOnDisplayChanged = { enabled ->
                            viewModel.setAlwaysOnDisplayEnabled(enabled)
                        },
                        onVolumeGestureChanged = { enabled ->
                            viewModel.setVolumeGestureEnabled(enabled)
                        },
                        onPeepProtectionChanged = { enabled ->
                            viewModel.setPeepProtectionEnabled(enabled)
                        },
                        onBiometricLockChanged = { enabled ->
                            viewModel.setBiometricLockEnabled(enabled)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                AppScreen.BlackOverlay -> {
                    BlackOverlayScreen(
                        overlayOpacityPercent = uiState.overlayOpacityPercent,
                        floatingButtonEnabled = uiState.floatingButtonEnabled,
                        onTapToUnlock = { viewModel.onBlackOverlayTapToUnlock() },
                        onFloatingButtonClick = { viewModel.exitBlackScreen() }
                    )
                }

                AppScreen.AODCustomization -> {
                    AODCustomizationScreen(
                        clockStyle = uiState.clockStyle,
                        selectedClockPreviewIndex = uiState.selectedClockPreviewIndex,
                        showDate = uiState.showDate,
                        showBattery = uiState.showBattery,
                        onClockStyleSelected = { style -> viewModel.setClockStyle(style) },
                        onClockPreviewSelected = { idx -> viewModel.setSelectedClockPreview(idx) },
                        onShowDateChanged = { show -> viewModel.setShowDate(show) },
                        onShowBatteryChanged = { show -> viewModel.setShowBattery(show) },
                        onApplyClick = { viewModel.applyAodConfiguration() },
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }

        if (uiState.showOverlayPermissionPrompt) {
            AlertDialog(
                onDismissRequest = { viewModel.setShowOverlayPermissionPrompt(false) },
                containerColor = PrimaryCardBg,
                titleContentColor = TextPrimary,
                textContentColor = TextSecondary,
                title = {
                    Text(text = "Floating Button Control")
                },
                text = {
                    Text(
                        text = "In-app floating button is active on the Black Screen Overlay. Grant system 'Display over other apps' permission to also show the floating button over external video apps."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.setShowOverlayPermissionPrompt(false)
                            onRequestSystemOverlayPermission()
                        }
                    ) {
                        Text(text = "Grant Permission", color = ElectricCyan)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.setShowOverlayPermissionPrompt(false)
                            viewModel.navigateTo(AppScreen.Settings)
                        }
                    ) {
                        Text(text = "Open Settings", color = Color.White)
                    }
                }
            )
        }
    }
}
