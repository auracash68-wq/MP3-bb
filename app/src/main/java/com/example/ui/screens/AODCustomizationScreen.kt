package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ClockStyle
import com.example.ui.components.AnalogClockFace
import com.example.ui.components.ClockPreviewCard
import com.example.ui.components.CustomSwitch
import com.example.ui.components.FlowingBlueWaveDecoration
import com.example.ui.components.PrimaryCyanButton
import com.example.ui.theme.CardBorderDark
import com.example.ui.theme.DarkTextOnCyan
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PrimaryCardBg
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AODCustomizationScreen(
    clockStyle: ClockStyle,
    selectedClockPreviewIndex: Int,
    showDate: Boolean,
    showBattery: Boolean,
    onClockStyleSelected: (ClockStyle) -> Unit,
    onClockPreviewSelected: (Int) -> Unit,
    onShowDateChanged: (Boolean) -> Unit,
    onShowBatteryChanged: (Boolean) -> Unit,
    onApplyClick: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("aod_customization_screen"),
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

            // Top Header: Back arrow + "AOD Customization"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("aod_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.aod_customization_title),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Section 1: Clock Style
            Text(
                text = stringResource(R.string.clock_style_label),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.5.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SegmentedClockStyleButton(
                    text = stringResource(R.string.clock_style_digital),
                    selected = clockStyle == ClockStyle.DIGITAL,
                    onClick = { onClockStyleSelected(ClockStyle.DIGITAL) },
                    modifier = Modifier.weight(1f),
                    testTag = "clock_style_digital_button"
                )
                SegmentedClockStyleButton(
                    text = stringResource(R.string.clock_style_analog),
                    selected = clockStyle == ClockStyle.ANALOG,
                    onClick = { onClockStyleSelected(ClockStyle.ANALOG) },
                    modifier = Modifier.weight(1f),
                    testTag = "clock_style_analog_button"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Clock Preview Selection (3 cards)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (idx in 0..2) {
                    ClockPreviewCard(
                        index = idx,
                        selected = selectedClockPreviewIndex == idx,
                        onClick = { onClockPreviewSelected(idx) },
                        modifier = Modifier.weight(1f),
                        testTag = "clock_preview_card_$idx"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Options (Show Date & Show Battery)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0E1013))
                    .border(1.dp, Color(0xFF22262C), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                // Row 1: Calendar icon + Show Date + Cyan ON switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.option_show_date),
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    CustomSwitch(
                        checked = showDate,
                        onCheckedChange = onShowDateChanged,
                        contentDesc = stringResource(R.string.option_show_date),
                        testTag = "switch_show_date"
                    )
                }

                HorizontalDivider(color = Color(0xFF20242A), thickness = 1.dp)

                // Row 2: Battery icon + Show Battery + Cyan ON switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalBatteryIcon(modifier = Modifier.size(width = 20.dp, height = 12.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.option_show_battery),
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    CustomSwitch(
                        checked = showBattery,
                        onCheckedChange = onShowBatteryChanged,
                        contentDesc = stringResource(R.string.option_show_battery),
                        testTag = "switch_show_battery"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Section 4: Live Preview (Smartphone-shaped black preview frame)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(188.dp)
                        .height(238.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                        .background(PureBlack)
                        .border(
                            width = 3.dp,
                            color = Color(0xFF454B54),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                        .testTag("aod_live_preview_frame")
                ) {
                    // Top speaker slot inside phone frame
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                            .width(34.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF2E333B))
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 34.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (clockStyle == ClockStyle.ANALOG || selectedClockPreviewIndex == 2) {
                            AnalogClockFace(modifier = Modifier.size(66.dp))
                        } else {
                            Text(
                                text = stringResource(R.string.preview_time_default),
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 40.sp,
                                lineHeight = 44.sp,
                                color = TextPrimary,
                                modifier = Modifier.testTag("aod_preview_clock_text")
                            )
                        }

                        if (showDate) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.preview_date_default),
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                modifier = Modifier.testTag("aod_preview_date_text")
                            )
                        }

                        if (showBattery) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.testTag("aod_preview_battery_row")
                            ) {
                                HorizontalBatteryIcon(
                                    modifier = Modifier.size(width = 16.dp, height = 9.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.preview_battery_default),
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Blue flowing wave wallpaper at bottom of preview phone
                        FlowingBlueWaveDecoration(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Large Cyan Rounded Apply Button
            PrimaryCyanButton(
                text = stringResource(R.string.action_apply),
                onClick = onApplyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                height = 50.dp,
                testTag = "aod_apply_button"
            )
        }
    }
}

@Composable
private fun SegmentedClockStyleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) ElectricCyan else PrimaryCardBg
            )
            .border(
                width = 1.dp,
                color = if (selected) ElectricCyan else CardBorderDark,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick, role = Role.Tab)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = InterFontFamily,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp,
            color = if (selected) DarkTextOnCyan else TextSecondary
        )
    }
}

@Composable
private fun HorizontalBatteryIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bodyW = w * 0.86f
        val tipW = w * 0.10f
        val tipH = h * 0.48f

        // Outer body
        drawRoundRect(
            color = TextPrimary,
            topLeft = Offset(0f, 0f),
            size = Size(bodyW, h),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(width = 1.3.dp.toPx())
        )
        // Inner fill (~80%)
        val pad = 2.dp.toPx()
        drawRoundRect(
            color = TextPrimary,
            topLeft = Offset(pad, pad),
            size = Size((bodyW - pad * 2) * 0.78f, h - pad * 2),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )
        // Right positive terminal tip
        drawRoundRect(
            color = TextPrimary,
            topLeft = Offset(bodyW + 1.dp.toPx(), (h - tipH) / 2f),
            size = Size(tipW, tipH),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )
    }
}
