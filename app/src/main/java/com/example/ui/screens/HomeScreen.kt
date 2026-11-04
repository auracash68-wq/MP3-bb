package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppScreen
import com.example.ui.components.FlowingBlueWaveDecoration
import com.example.ui.theme.BatteryGreen
import com.example.ui.theme.BlueGlow
import com.example.ui.theme.BrightCyan
import com.example.ui.theme.DarkTextOnCyan
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PrimaryCardBg
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    batterySavedPercent: Int,
    floatingButtonEnabled: Boolean,
    showScreenJumpSheet: Boolean,
    onMenuClick: () -> Unit,
    onDismissScreenJumpSheet: () -> Unit,
    onJumpToScreen: (AppScreen) -> Unit,
    onSettingsClick: () -> Unit,
    onActivateBlackScreen: () -> Unit,
    onFloatingButtonShortcutClick: () -> Unit,
    onAodShortcutClick: () -> Unit,
    onQuickTileShortcutClick: () -> Unit,
    onPromoCardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("home_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Top Navigation Bar: Hamburger Menu + 2-line Title + Settings Gear
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("home_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Navigation Menu",
                            tint = TextPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.app_title_line1),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 20.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = stringResource(R.string.app_title_line2),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 20.sp,
                            color = ElectricCyan
                        )
                    }

                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Main Action: Large Cyan Glowing Rounded Button "Activate Black Screen"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .drawBehind {
                            drawRoundRect(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        ElectricCyan.copy(alpha = 0.48f),
                                        BlueGlow.copy(alpha = 0.22f),
                                        Color.Transparent
                                    ),
                                    center = Offset(size.width / 2f, size.height / 2f),
                                    radius = size.width * 0.62f
                                ),
                                topLeft = Offset(-12.dp.toPx(), -14.dp.toPx()),
                                size = Size(size.width + 24.dp.toPx(), size.height + 28.dp.toPx()),
                                cornerRadius = CornerRadius(50.dp.toPx(), 50.dp.toPx())
                            )
                        }
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(ElectricCyan, BrightCyan, ElectricCyan)
                            )
                        )
                        .border(1.5.dp, Color(0xFF8CF0FF), RoundedCornerShape(50))
                        .clickable(onClick = onActivateBlackScreen, role = Role.Button)
                        .testTag("activate_black_screen_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Black Play Triangle Icon
                        Canvas(modifier = Modifier.size(26.dp)) {
                            val p = Path().apply {
                                moveTo(size.width * 0.18f, size.height * 0.12f)
                                lineTo(size.width * 0.18f, size.height * 0.88f)
                                lineTo(size.width * 0.88f, size.height * 0.50f)
                                close()
                            }
                            drawPath(path = p, color = DarkTextOnCyan)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = stringResource(R.string.activate_black_screen),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 22.sp,
                            color = DarkTextOnCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Three Equally Sized Dark Shortcut Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Floating Button
                    ShortcutCard(
                        label = stringResource(R.string.shortcut_floating_button),
                        onClick = onFloatingButtonShortcutClick,
                        modifier = Modifier.weight(1f),
                        isActiveHighlight = floatingButtonEnabled,
                        testTag = "shortcut_floating_button_card"
                    ) {
                        GlowingBluePlayCircleIcon(modifier = Modifier.size(38.dp))
                    }

                    // Card 2: AOD
                    ShortcutCard(
                        label = stringResource(R.string.shortcut_aod),
                        onClick = onAodShortcutClick,
                        modifier = Modifier.weight(1f),
                        testTag = "shortcut_aod_card"
                    ) {
                        CyanCrescentMoonIcon(modifier = Modifier.size(34.dp))
                    }

                    // Card 3: Quick Tile
                    ShortcutCard(
                        label = stringResource(R.string.shortcut_quick_tile),
                        onClick = onQuickTileShortcutClick,
                        modifier = Modifier.weight(1f),
                        testTag = "shortcut_quick_tile_card"
                    ) {
                        CyanFourSquareGridIcon(modifier = Modifier.size(32.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Battery Saved Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(PrimaryCardBg)
                        .border(1.dp, Color(0xFF282C34), RoundedCornerShape(16.dp))
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                        .testTag("battery_saved_card"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Green Battery Outline with 3 charge bars
                    GreenBatteryOutlineIcon(
                        modifier = Modifier.size(width = 32.dp, height = 52.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Middle Label & Large Green Percentage
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.battery_saved_label),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.5.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$batterySavedPercent%",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 33.sp,
                            lineHeight = 36.sp,
                            color = BatteryGreen
                        )
                    }

                    // Right Green Signal Bars
                    GreenSignalBarsIcon(
                        modifier = Modifier
                            .size(width = 44.dp, height = 34.dp)
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Promotional Card: Green leaf + "Save Battery, Go Further!" + Right arrow
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PrimaryCardBg)
                        .border(1.dp, Color(0xFF282C34), RoundedCornerShape(16.dp))
                        .clickable(onClick = onPromoCardClick, role = Role.Button)
                        .padding(horizontal = 16.dp)
                        .testTag("promo_save_battery_card"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GreenLeafSmallIcon(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.promo_save_battery),
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom subtle cyan/blue flowing wave decoration above navigation bar
            FlowingBlueWaveDecoration(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("home_bottom_wave")
            )
        }

        if (showScreenJumpSheet) {
            ModalBottomSheet(
                onDismissRequest = onDismissScreenJumpSheet,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = PrimaryCardBg,
                contentColor = TextPrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "All 8 Reference Screens",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = ElectricCyan,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    val items = listOf(
                        "01 — Splash Screen" to AppScreen.Splash,
                        "02 — Onboarding 01 (Play Videos)" to AppScreen.Onboarding1,
                        "03 — Onboarding 02 (AMOLED Battery Saver)" to AppScreen.Onboarding2,
                        "04 — Onboarding 03 (Quick Floating Button)" to AppScreen.Onboarding3,
                        "05 — Home Dashboard" to AppScreen.Home,
                        "06 — Settings" to AppScreen.Settings,
                        "07 — Active Black Screen Overlay" to AppScreen.BlackOverlay,
                        "08 — AOD Customization" to AppScreen.AODCustomization
                    )
                    items.forEach { (label, screen) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF24272D))
                                .clickable { onJumpToScreen(screen) }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = label,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ShortcutCard(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActiveHighlight: Boolean = false,
    testTag: String,
    icon: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .height(114.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryCardBg)
            .border(
                width = 1.dp,
                color = if (isActiveHighlight) Color(0xFF333A45) else Color(0xFF282C34),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick, role = Role.Button)
            .padding(horizontal = 8.dp, vertical = 14.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = label,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.5.sp,
            lineHeight = 15.sp,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GlowingBluePlayCircleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val r = size.minDimension / 2f - 3.dp.toPx()

        // Outer soft blue glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricCyan.copy(alpha = 0.45f),
                    DeepBlue.copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = c,
                radius = size.minDimension / 2f
            ),
            radius = size.minDimension / 2f,
            center = c
        )

        // Blue fill
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF2B88FF), Color(0xFF0E53C4))
            ),
            radius = r,
            center = c
        )

        // Cyan rim
        drawCircle(
            color = ElectricCyan.copy(alpha = 0.85f),
            radius = r,
            center = c,
            style = Stroke(width = 1.5.dp.toPx())
        )

        // White Play Triangle
        val pr = r * 0.44f
        val p = Path().apply {
            moveTo(c.x - pr * 0.42f, c.y - pr * 0.65f)
            lineTo(c.x - pr * 0.42f, c.y + pr * 0.65f)
            lineTo(c.x + pr * 0.72f, c.y)
            close()
        }
        drawPath(path = p, color = TextPrimary)
    }
}

@Composable
fun CyanCrescentMoonIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val moonPath = Path().apply {
            moveTo(w * 0.56f, h * 0.14f)
            cubicTo(
                w * 0.18f, h * 0.18f,
                w * 0.12f, h * 0.74f,
                w * 0.52f, h * 0.86f
            )
            cubicTo(
                w * 0.74f, h * 0.92f,
                w * 0.90f, h * 0.78f,
                w * 0.90f, h * 0.68f
            )
            cubicTo(
                w * 0.62f, h * 0.76f,
                w * 0.42f, h * 0.52f,
                w * 0.56f, h * 0.14f
            )
            close()
        }
        drawPath(
            path = moonPath,
            brush = Brush.linearGradient(
                colors = listOf(ElectricCyan, Color(0xFF1A9CFF))
            )
        )
    }
}

@Composable
fun CyanFourSquareGridIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val gap = 4.dp.toPx()
        val pad = 3.dp.toPx()
        val cell = (size.minDimension - pad * 2 - gap) / 2f
        val r = CornerRadius(3.dp.toPx(), 3.dp.toPx())

        for (row in 0..1) {
            for (col in 0..1) {
                val x = pad + col * (cell + gap)
                val y = pad + row * (cell + gap)
                drawRoundRect(
                    color = ElectricCyan,
                    topLeft = Offset(x, y),
                    size = Size(cell, cell),
                    cornerRadius = r
                )
            }
        }
    }
}

@Composable
fun GreenBatteryOutlineIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val capW = w * 0.44f
        val capH = 4.dp.toPx()
        val bodyTop = capH + 1.dp.toPx()
        val bodyH = h - bodyTop

        // Top terminal cap
        drawRoundRect(
            color = BatteryGreen,
            topLeft = Offset((w - capW) / 2f, 0f),
            size = Size(capW, capH),
            cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())
        )

        // Outer green rounded rectangle outline
        drawRoundRect(
            color = BatteryGreen,
            topLeft = Offset(1.dp.toPx(), bodyTop),
            size = Size(w - 2.dp.toPx(), bodyH),
            cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
            style = Stroke(width = 2.2.dp.toPx())
        )

        // 3 interior green charge bars
        val inset = 5.5.dp.toPx()
        val barGap = 3.dp.toPx()
        val totalBarsH = bodyH - inset * 2 - barGap * 2
        val singleBarH = totalBarsH / 3f
        for (i in 0..2) {
            val barY = bodyTop + inset + i * (singleBarH + barGap)
            drawRoundRect(
                color = BatteryGreen,
                topLeft = Offset(inset, barY),
                size = Size(w - inset * 2, singleBarH),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }
    }
}

@Composable
fun GreenSignalBarsIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val barCount = 4
        val gap = 5.dp.toPx()
        val barW = (w - gap * (barCount - 1)) / barCount
        val heights = floatArrayOf(0.24f, 0.46f, 0.72f, 1.0f)

        for (i in 0 until barCount) {
            val bh = h * heights[i]
            val bx = i * (barW + gap)
            val by = h - bh
            drawRoundRect(
                color = BatteryGreen,
                topLeft = Offset(bx, by),
                size = Size(barW, bh),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }
    }
}

@Composable
fun GreenLeafSmallIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val p = Path().apply {
            moveTo(w * 0.16f, h * 0.84f)
            cubicTo(
                w * 0.14f, h * 0.36f,
                w * 0.50f, h * 0.14f,
                w * 0.88f, h * 0.16f
            )
            cubicTo(
                w * 0.86f, h * 0.56f,
                w * 0.58f, h * 0.86f,
                w * 0.16f, h * 0.84f
            )
            close()
        }
        drawPath(path = p, color = BatteryGreen)
        drawLine(
            color = Color(0xFF0F5E18),
            start = Offset(w * 0.14f, h * 0.86f),
            end = Offset(w * 0.72f, h * 0.32f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
