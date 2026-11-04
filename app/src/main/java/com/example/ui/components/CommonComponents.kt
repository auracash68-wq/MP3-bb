package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueGlow
import com.example.ui.theme.BrightCyan
import com.example.ui.theme.CardBorderDark
import com.example.ui.theme.DarkTextOnCyan
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PrimaryCardBg
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PrimaryCyanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 50.dp,
    glowEnabled: Boolean = true,
    testTag: String = "primary_cyan_button"
) {
    Box(
        modifier = modifier
            .height(height)
            .drawBehind {
                if (glowEnabled) {
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                ElectricCyan.copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = size.width * 0.65f
                        )
                    )
                }
            }
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(ElectricCyan, BrightCyan)
                )
            )
            .clickable(onClick = onClick, role = Role.Button)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = DarkTextOnCyan
        )
    }
}

@Composable
fun DarkPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 50.dp,
    testTag: String = "dark_pill_button"
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF14161A))
            .border(1.dp, Color(0xFF282C34), RoundedCornerShape(50))
            .clickable(onClick = onClick, role = Role.Button)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = TextPrimary
        )
    }
}

@Composable
fun DarkSettingCard(
    title: String,
    subtitle: String,
    iconContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bottomContent: (@Composable () -> Unit)? = null,
    testTag: String = "dark_setting_card"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryCardBg)
            .border(1.dp, Color(0xFF25282E), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                iconContent()
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            trailingContent()
        }
        if (bottomContent != null) {
            Spacer(modifier = Modifier.height(10.dp))
            bottomContent()
        }
    }
}

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentDesc: String = "Toggle switch",
    testTag: String = "custom_switch"
) {
    val thumbProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 180),
        label = "switch_thumb"
    )

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .semantics { contentDescription = contentDesc }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(46.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    if (checked) ElectricCyan else Color(0xFF2E333B)
                )
                .padding(horizontal = 3.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = (20f * thumbProgress).dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        if (checked) TextPrimary else Color(0xFF9AA0A6)
                    )
            )
        }
    }
}

@Composable
fun CustomSlider(
    valuePercent: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minPercent: Int = 20,
    maxPercent: Int = 100,
    testTag: String = "custom_slider"
) {
    var widthPx by remember { mutableFloatStateOf(1f) }
    val fraction = ((valuePercent - minPercent).toFloat() / (maxPercent - minPercent).toFloat())
        .coerceIn(0f, 1f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 52.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
                .onSizeChanged { widthPx = it.width.toFloat().coerceAtLeast(1f) }
                .pointerInput(minPercent, maxPercent) {
                    detectTapGestures { offset ->
                        val newFrac = (offset.x / widthPx).coerceIn(0f, 1f)
                        val newVal = (minPercent + newFrac * (maxPercent - minPercent)).roundToInt()
                        onValueChange(newVal)
                    }
                }
                .pointerInput(minPercent, maxPercent) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        val newFrac = (change.position.x / widthPx).coerceIn(0f, 1f)
                        val newVal = (minPercent + newFrac * (maxPercent - minPercent)).roundToInt()
                        onValueChange(newVal)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val trackY = size.height / 2f
                val thumbRadius = 8.dp.toPx()
                val usableWidth = (size.width - thumbRadius * 2).coerceAtLeast(1f)
                val thumbX = thumbRadius + usableWidth * fraction

                // Inactive track
                drawLine(
                    color = Color(0xFF2D3139),
                    start = Offset(thumbRadius, trackY),
                    end = Offset(size.width - thumbRadius, trackY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Active cyan track
                drawLine(
                    color = ElectricCyan,
                    start = Offset(thumbRadius, trackY),
                    end = Offset(thumbX, trackY),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Subtle cyan halo around thumb
                drawCircle(
                    color = ElectricCyan.copy(alpha = 0.28f),
                    radius = 12.dp.toPx(),
                    center = Offset(thumbX, trackY)
                )

                // Cyan thumb
                drawCircle(
                    color = ElectricCyan,
                    radius = 7.5.dp.toPx(),
                    center = Offset(thumbX, trackY)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$valuePercent%",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = TextPrimary
        )
    }
}

@Composable
fun ClockPreviewCard(
    index: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "clock_preview_card_$index"
) {
    Box(
        modifier = modifier
            .height(92.dp)
            .clickable(onClick = onClick, role = Role.RadioButton)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF141619))
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) ElectricCyan else CardBorderDark,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            when (index) {
                0 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "09",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                            lineHeight = 24.sp,
                            color = ElectricCyan,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "41",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                            lineHeight = 24.sp,
                            color = ElectricCyan,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                1 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "09",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                            lineHeight = 24.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "41",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                            lineHeight = 24.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    AnalogClockFace(
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-6).dp, y = 4.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(ElectricCyan)
                    .border(1.5.dp, PureBlack, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = DarkTextOnCyan,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
fun AnalogClockFace(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2f - 2.dp.toPx()

        drawCircle(
            color = Color(0xFF0B0D10),
            radius = radius,
            center = center
        )
        drawCircle(
            color = Color(0xFF2F343C),
            radius = radius,
            center = center,
            style = Stroke(width = 1.5.dp.toPx())
        )

        // 12 hour ticks
        for (i in 0 until 12) {
            val angleRad = (i * 30.0 - 90.0) * (PI / 180.0)
            val isCardinal = i % 3 == 0
            val outerR = radius - 3.dp.toPx()
            val innerR = outerR - if (isCardinal) 4.5.dp.toPx() else 2.5.dp.toPx()
            val start = Offset(
                x = center.x + (innerR * cos(angleRad)).toFloat(),
                y = center.y + (innerR * sin(angleRad)).toFloat()
            )
            val end = Offset(
                x = center.x + (outerR * cos(angleRad)).toFloat(),
                y = center.y + (outerR * sin(angleRad)).toFloat()
            )
            drawLine(
                color = if (isCardinal) TextPrimary else Color(0xFF8A8A8A),
                start = start,
                end = end,
                strokeWidth = if (isCardinal) 1.6.dp.toPx() else 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Hour hand (~10 o'clock)
        val hourAngle = (-145.0) * (PI / 180.0)
        drawLine(
            color = TextPrimary,
            start = center,
            end = Offset(
                center.x + (radius * 0.45f * cos(hourAngle)).toFloat(),
                center.y + (radius * 0.45f * sin(hourAngle)).toFloat()
            ),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Minute hand (~2 o'clock)
        val minAngle = (-35.0) * (PI / 180.0)
        drawLine(
            color = TextPrimary,
            start = center,
            end = Offset(
                center.x + (radius * 0.68f * cos(minAngle)).toFloat(),
                center.y + (radius * 0.68f * sin(minAngle)).toFloat()
            ),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Cyan second hand (~7 o'clock)
        val secAngle = (135.0) * (PI / 180.0)
        drawLine(
            color = ElectricCyan,
            start = center,
            end = Offset(
                center.x + (radius * 0.72f * cos(secAngle)).toFloat(),
                center.y + (radius * 0.72f * sin(secAngle)).toFloat()
            ),
            strokeWidth = 1.2.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawCircle(
            color = TextPrimary,
            radius = 2.2.dp.toPx(),
            center = center
        )
    }
}

@Composable
fun FlowingBlueWaveDecoration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Ambient soft blue glow behind waves
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    DeepBlue.copy(alpha = 0.22f),
                    ElectricCyan.copy(alpha = 0.14f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = h
            )
        )

        // Wave fill 1
        val fillPath1 = Path().apply {
            moveTo(0f, h * 0.62f)
            cubicTo(
                w * 0.28f, h * 0.15f,
                w * 0.65f, h * 0.95f,
                w, h * 0.35f
            )
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = fillPath1,
            brush = Brush.verticalGradient(
                colors = listOf(
                    BlueGlow.copy(alpha = 0.25f),
                    DeepBlue.copy(alpha = 0.05f)
                )
            )
        )

        // Wave stroke 1 (bright cyan crest)
        val wave1 = Path().apply {
            moveTo(0f, h * 0.62f)
            cubicTo(
                w * 0.28f, h * 0.15f,
                w * 0.65f, h * 0.95f,
                w, h * 0.35f
            )
        }
        drawPath(
            path = wave1,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    DeepBlue.copy(alpha = 0.4f),
                    ElectricCyan.copy(alpha = 0.9f),
                    BlueGlow.copy(alpha = 0.5f)
                )
            ),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Wave stroke 2 (secondary intersecting silk wave)
        val wave2 = Path().apply {
            moveTo(0f, h * 0.42f)
            cubicTo(
                w * 0.35f, h * 0.88f,
                w * 0.72f, h * 0.18f,
                w, h * 0.68f
            )
        }
        drawPath(
            path = wave2,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    ElectricCyan.copy(alpha = 0.35f),
                    BlueGlow.copy(alpha = 0.75f),
                    DeepBlue.copy(alpha = 0.35f)
                )
            ),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
