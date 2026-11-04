package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CustomSlider
import com.example.ui.components.CustomSwitch
import com.example.ui.components.DarkSettingCard
import com.example.ui.theme.DarkTextOnCyan
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary

@Composable
fun SettingsScreen(
    floatingButtonEnabled: Boolean,
    overlayOpacityPercent: Int,
    alwaysOnDisplayEnabled: Boolean,
    volumeGestureEnabled: Boolean,
    peepProtectionEnabled: Boolean,
    biometricLockEnabled: Boolean,
    onFloatingButtonChanged: (Boolean) -> Unit,
    onOverlayOpacityChanged: (Int) -> Unit,
    onAlwaysOnDisplayChanged: (Boolean) -> Unit,
    onVolumeGestureChanged: (Boolean) -> Unit,
    onPeepProtectionChanged: (Boolean) -> Unit,
    onBiometricLockChanged: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("settings_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Top Header: Back arrow + "Settings"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("settings_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.settings_title),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stacked Rounded Dark Grey Setting Cards in Exact Reference Order
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // 1. Floating Button
                DarkSettingCard(
                    title = stringResource(R.string.setting_floating_button_title),
                    subtitle = stringResource(R.string.setting_floating_button_sub),
                    iconContent = {
                        GlowingBluePlayCircleIcon(modifier = Modifier.size(36.dp))
                    },
                    trailingContent = {
                        CustomSwitch(
                            checked = floatingButtonEnabled,
                            onCheckedChange = onFloatingButtonChanged,
                            contentDesc = stringResource(R.string.setting_floating_button_title),
                            testTag = "switch_floating_button"
                        )
                    },
                    testTag = "card_floating_button"
                )

                // 2. Overlay Opacity (with Water-drop icon + Horizontal cyan slider + 100%)
                DarkSettingCard(
                    title = stringResource(R.string.setting_overlay_opacity_title),
                    subtitle = stringResource(R.string.setting_overlay_opacity_sub),
                    iconContent = {
                        WaterDropSettingIcon(modifier = Modifier.size(36.dp))
                    },
                    trailingContent = {},
                    bottomContent = {
                        CustomSlider(
                            valuePercent = overlayOpacityPercent,
                            onValueChange = onOverlayOpacityChanged,
                            testTag = "slider_overlay_opacity"
                        )
                    },
                    testTag = "card_overlay_opacity"
                )

                // 3. Always-On Display
                DarkSettingCard(
                    title = stringResource(R.string.setting_aod_title),
                    subtitle = stringResource(R.string.setting_aod_sub),
                    iconContent = {
                        BlueClockSettingIcon(modifier = Modifier.size(36.dp))
                    },
                    trailingContent = {
                        CustomSwitch(
                            checked = alwaysOnDisplayEnabled,
                            onCheckedChange = onAlwaysOnDisplayChanged,
                            contentDesc = stringResource(R.string.setting_aod_title),
                            testTag = "switch_always_on_display"
                        )
                    },
                    testTag = "card_always_on_display"
                )

                // 4. Volume Gesture
                DarkSettingCard(
                    title = stringResource(R.string.setting_volume_gesture_title),
                    subtitle = stringResource(R.string.setting_volume_gesture_sub),
                    iconContent = {
                        BlueSpeakerSettingIcon(modifier = Modifier.size(36.dp))
                    },
                    trailingContent = {
                        CustomSwitch(
                            checked = volumeGestureEnabled,
                            onCheckedChange = onVolumeGestureChanged,
                            contentDesc = stringResource(R.string.setting_volume_gesture_title),
                            testTag = "switch_volume_gesture"
                        )
                    },
                    testTag = "card_volume_gesture"
                )

                // 5. Peep Protection
                DarkSettingCard(
                    title = stringResource(R.string.setting_peep_protection_title),
                    subtitle = stringResource(R.string.setting_peep_protection_sub),
                    iconContent = {
                        CyanShieldSettingIcon(modifier = Modifier.size(36.dp))
                    },
                    trailingContent = {
                        CustomSwitch(
                            checked = peepProtectionEnabled,
                            onCheckedChange = onPeepProtectionChanged,
                            contentDesc = stringResource(R.string.setting_peep_protection_title),
                            testTag = "switch_peep_protection"
                        )
                    },
                    testTag = "card_peep_protection"
                )

                // 6. Biometric Lock
                DarkSettingCard(
                    title = stringResource(R.string.setting_biometric_lock_title),
                    subtitle = stringResource(R.string.setting_biometric_lock_sub),
                    iconContent = {
                        GreyFingerprintSettingIcon(modifier = Modifier.size(36.dp))
                    },
                    trailingContent = {
                        CustomSwitch(
                            checked = biometricLockEnabled,
                            onCheckedChange = onBiometricLockChanged,
                            contentDesc = stringResource(R.string.setting_biometric_lock_title),
                            testTag = "switch_biometric_lock"
                        )
                    },
                    testTag = "card_biometric_lock"
                )
            }
        }
    }
}

@Composable
private fun WaterDropSettingIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2f
        drawCircle(
            color = Color(0xFF282C34),
            radius = r,
            center = c
        )
        val w = size.width
        val h = size.height
        val dropPath = Path().apply {
            moveTo(w * 0.50f, h * 0.18f)
            cubicTo(
                w * 0.30f, h * 0.45f,
                w * 0.24f, h * 0.62f,
                w * 0.24f, h * 0.70f
            )
            cubicTo(
                w * 0.24f, h * 0.85f,
                w * 0.36f, h * 0.90f,
                w * 0.50f, h * 0.90f
            )
            cubicTo(
                w * 0.64f, h * 0.90f,
                w * 0.76f, h * 0.85f,
                w * 0.76f, h * 0.70f
            )
            cubicTo(
                w * 0.76f, h * 0.62f,
                w * 0.70f, h * 0.45f,
                w * 0.50f, h * 0.18f
            )
            close()
        }
        drawPath(
            path = dropPath,
            brush = Brush.linearGradient(
                colors = listOf(Color.White, Color(0xFFB5BDC9))
            )
        )
    }
}

@Composable
private fun BlueClockSettingIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2f
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF26A0FF), Color(0xFF1262D6))
            ),
            radius = r,
            center = c
        )
        // White inner clock circle
        drawCircle(
            color = TextPrimary,
            radius = r * 0.58f,
            center = c
        )
        // Blue clock hands
        drawLine(
            color = Color(0xFF1262D6),
            start = c,
            end = Offset(c.x, c.y - r * 0.36f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF1262D6),
            start = c,
            end = Offset(c.x + r * 0.28f, c.y),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun BlueSpeakerSettingIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2A85FF), Color(0xFF1458CC))
                ),
                radius = size.minDimension / 2f
            )
        }
        Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = null,
            tint = TextPrimary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun CyanShieldSettingIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val shield = Path().apply {
            moveTo(w * 0.50f, h * 0.08f)
            lineTo(w * 0.88f, h * 0.22f)
            lineTo(w * 0.88f, h * 0.52f)
            cubicTo(
                w * 0.88f, h * 0.76f,
                w * 0.68f, h * 0.90f,
                w * 0.50f, h * 0.96f
            )
            cubicTo(
                w * 0.32f, h * 0.90f,
                w * 0.12f, h * 0.76f,
                w * 0.12f, h * 0.52f
            )
            lineTo(w * 0.12f, h * 0.22f)
            close()
        }
        drawPath(
            path = shield,
            brush = Brush.verticalGradient(
                colors = listOf(ElectricCyan, Color(0xFF00A8E8))
            )
        )

        // Checkmark inside shield
        val check = Path().apply {
            moveTo(w * 0.34f, h * 0.52f)
            lineTo(w * 0.46f, h * 0.64f)
            lineTo(w * 0.68f, h * 0.40f)
        }
        drawPath(
            path = check,
            color = DarkTextOnCyan,
            style = Stroke(width = 2.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
private fun GreyFingerprintSettingIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFF6B717B),
                radius = size.minDimension / 2f
            )
        }
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = null,
            tint = Color(0xFFE0E4EB),
            modifier = Modifier.size(24.dp)
        )
    }
}
