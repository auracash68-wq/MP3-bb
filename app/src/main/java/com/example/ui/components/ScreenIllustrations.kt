package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BatteryGreen
import com.example.ui.theme.BatteryGreenDark
import com.example.ui.theme.BlueGlow
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import kotlin.math.abs

@Composable
fun SplashLogoIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        // Soft ambient cyan glow behind squircle
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricCyan.copy(alpha = 0.25f),
                    DeepBlue.copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = size.minDimension * 0.54f
            ),
            radius = size.minDimension * 0.54f,
            center = Offset(cx, cy)
        )

        val squircleSize = size.minDimension * 0.66f
        val squircleLeft = cx - squircleSize / 2f
        val squircleTop = cy - squircleSize / 2f

        // Back half of orbital ring (tilted -16 degrees)
        rotate(degrees = -16f, pivot = Offset(cx, cy)) {
            val orbitRect = Rect(
                left = cx - squircleSize * 0.68f,
                top = cy - squircleSize * 0.20f,
                right = cx + squircleSize * 0.68f,
                bottom = cy + squircleSize * 0.24f
            )
            drawArc(
                color = ElectricCyan.copy(alpha = 0.45f),
                startAngle = 185f,
                sweepAngle = 170f,
                useCenter = false,
                topLeft = orbitRect.topLeft,
                size = orbitRect.size,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Cyan rounded squircle outline
        drawRoundRect(
            color = PureBlack.copy(alpha = 0.85f),
            topLeft = Offset(squircleLeft, squircleTop),
            size = Size(squircleSize, squircleSize),
            cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx())
        )
        drawRoundRect(
            color = ElectricCyan,
            topLeft = Offset(squircleLeft, squircleTop),
            size = Size(squircleSize, squircleSize),
            cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )

        // Inner smartphone body
        val phoneW = squircleSize * 0.48f
        val phoneH = squircleSize * 0.78f
        val phoneLeft = cx - phoneW / 2f
        val phoneTop = cy - phoneH / 2f

        drawRoundRect(
            color = Color(0xFF06080B),
            topLeft = Offset(phoneLeft, phoneTop),
            size = Size(phoneW, phoneH),
            cornerRadius = CornerRadius(11.dp.toPx(), 11.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFFD0D5DD),
            topLeft = Offset(phoneLeft, phoneTop),
            size = Size(phoneW, phoneH),
            cornerRadius = CornerRadius(11.dp.toPx(), 11.dp.toPx()),
            style = Stroke(width = 2.dp.toPx())
        )

        // Top speaker notch inside smartphone
        drawLine(
            color = Color(0xFF7A828E),
            start = Offset(cx - phoneW * 0.16f, phoneTop + 6.dp.toPx()),
            end = Offset(cx + phoneW * 0.16f, phoneTop + 6.dp.toPx()),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Centered White Play Triangle
        val playW = phoneW * 0.36f
        val playH = phoneW * 0.42f
        val playPath = Path().apply {
            moveTo(cx - playW * 0.38f, cy - playH / 2f)
            lineTo(cx - playW * 0.38f, cy + playH / 2f)
            lineTo(cx + playW * 0.62f, cy)
            close()
        }
        drawPath(path = playPath, color = TextPrimary)

        // Front half of glowing cyan orbital ring swooshing across the front
        rotate(degrees = -16f, pivot = Offset(cx, cy)) {
            val orbitRect = Rect(
                left = cx - squircleSize * 0.70f,
                top = cy - squircleSize * 0.18f,
                right = cx + squircleSize * 0.70f,
                bottom = cy + squircleSize * 0.25f
            )
            // Outer glow
            drawArc(
                color = ElectricCyan.copy(alpha = 0.35f),
                startAngle = -10f,
                sweepAngle = 200f,
                useCenter = false,
                topLeft = orbitRect.topLeft,
                size = orbitRect.size,
                style = Stroke(width = 11.dp.toPx(), cap = StrokeCap.Round)
            )
            // Main cyan swoosh arc
            drawArc(
                brush = Brush.horizontalGradient(
                    colors = listOf(ElectricCyan, Color(0xFF68E8FF), ElectricCyan)
                ),
                startAngle = -5f,
                sweepAngle = 190f,
                useCenter = false,
                topLeft = orbitRect.topLeft,
                size = orbitRect.size,
                style = Stroke(width = 5.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun SplashSegmentedSpinner(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinner_rotation"
    )

    Canvas(modifier = modifier) {
        rotate(degrees = angle) {
            val stroke = 2.4.dp.toPx()
            val inset = stroke / 2f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(inset, inset)

            // Cyan top-left segments
            drawArc(
                color = ElectricCyan,
                startAngle = 190f,
                sweepAngle = 55f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = ElectricCyan.copy(alpha = 0.75f),
                startAngle = 265f,
                sweepAngle = 35f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            // White bottom-right segments
            drawArc(
                color = TextPrimary,
                startAngle = 320f,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = TextPrimary.copy(alpha = 0.65f),
                startAngle = 100f,
                sweepAngle = 55f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    }
}

private fun DrawScope.drawGlowingPlatform(
    cx: Float,
    platformY: Float,
    platformWidth: Float,
    platformHeight: Float
) {
    // Broad outer blue radial glow
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                ElectricCyan.copy(alpha = 0.32f),
                DeepBlue.copy(alpha = 0.18f),
                Color.Transparent
            ),
            center = Offset(cx, platformY),
            radius = platformWidth * 0.62f
        ),
        topLeft = Offset(cx - platformWidth * 0.62f, platformY - platformHeight * 1.15f),
        size = Size(platformWidth * 1.24f, platformHeight * 2.3f)
    )

    // Outer cyan ring
    drawOval(
        brush = Brush.horizontalGradient(
            colors = listOf(
                DeepBlue.copy(alpha = 0.4f),
                ElectricCyan.copy(alpha = 0.9f),
                DeepBlue.copy(alpha = 0.4f)
            )
        ),
        topLeft = Offset(cx - platformWidth * 0.48f, platformY - platformHeight * 0.5f),
        size = Size(platformWidth * 0.96f, platformHeight),
        style = Stroke(width = 2.dp.toPx())
    )

    // Inner bright cyan ring
    drawOval(
        brush = Brush.horizontalGradient(
            colors = listOf(
                ElectricCyan.copy(alpha = 0.6f),
                Color(0xFF88F2FF),
                ElectricCyan.copy(alpha = 0.6f)
            )
        ),
        topLeft = Offset(cx - platformWidth * 0.36f, platformY - platformHeight * 0.35f),
        size = Size(platformWidth * 0.72f, platformHeight * 0.7f),
        style = Stroke(width = 2.2.dp.toPx())
    )
}

private fun DrawScope.drawReferenceSmartphone(
    cx: Float,
    phoneTop: Float,
    phoneW: Float,
    phoneH: Float,
    drawCenterPlayIcon: Boolean
) {
    val phoneLeft = cx - phoneW / 2f
    val corner = CornerRadius(18.dp.toPx(), 18.dp.toPx())

    // Outer metallic frame
    drawRoundRect(
        color = Color(0xFF050608),
        topLeft = Offset(phoneLeft, phoneTop),
        size = Size(phoneW, phoneH),
        cornerRadius = corner
    )
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF9EA6B2), Color(0xFF525862), Color(0xFF8A929E))
        ),
        topLeft = Offset(phoneLeft, phoneTop),
        size = Size(phoneW, phoneH),
        cornerRadius = corner,
        style = Stroke(width = 2.5.dp.toPx())
    )

    // Inner dark screen border
    val inset = 4.dp.toPx()
    drawRoundRect(
        color = PureBlack,
        topLeft = Offset(phoneLeft + inset, phoneTop + inset),
        size = Size(phoneW - inset * 2, phoneH - inset * 2),
        cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx())
    )

    // Top speaker slot
    drawLine(
        color = Color(0xFF3A3F47),
        start = Offset(cx - phoneW * 0.13f, phoneTop + 10.dp.toPx()),
        end = Offset(cx + phoneW * 0.13f, phoneTop + 10.dp.toPx()),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )

    if (drawCenterPlayIcon) {
        val playCenterY = phoneTop + phoneH * 0.5f
        val playW = phoneW * 0.22f
        val playH = phoneW * 0.26f
        val playPath = Path().apply {
            moveTo(cx - playW * 0.38f, playCenterY - playH / 2f)
            lineTo(cx - playW * 0.38f, playCenterY + playH / 2f)
            lineTo(cx + playW * 0.62f, playCenterY)
            close()
        }
        drawPath(
            path = playPath,
            color = TextPrimary
        )
    }
}

@Composable
fun Onboarding1Illustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val h = size.height
        val phoneW = size.width * 0.36f
        val phoneH = h * 0.74f
        val phoneTop = h * 0.08f
        val platformY = phoneTop + phoneH + h * 0.02f

        // Glowing blue elliptical platform underneath
        drawGlowingPlatform(
            cx = cx,
            platformY = platformY,
            platformWidth = size.width * 0.78f,
            platformHeight = h * 0.17f
        )

        // Cyan and blue audio waveform bars on both sides
        val waveCenterY = phoneTop + phoneH * 0.5f
        val barHeights = floatArrayOf(0.10f, 0.22f, 0.34f, 0.18f, 0.28f, 0.15f, 0.08f)
        val barSpacing = 9.dp.toPx()
        val barStroke = 3.8.dp.toPx()
        val startOffsetFromPhone = phoneW / 2f + 12.dp.toPx()

        for (i in barHeights.indices) {
            val halfBarH = (phoneH * barHeights[i]) / 2f
            val leftX = cx - startOffsetFromPhone - i * barSpacing
            val rightX = cx + startOffsetFromPhone + i * barSpacing
            val barColor = if (i % 2 == 0) ElectricCyan else BlueGlow

            // Glow
            drawLine(
                color = barColor.copy(alpha = 0.28f),
                start = Offset(leftX, waveCenterY - halfBarH),
                end = Offset(leftX, waveCenterY + halfBarH),
                strokeWidth = barStroke * 2.2f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = barColor.copy(alpha = 0.28f),
                start = Offset(rightX, waveCenterY - halfBarH),
                end = Offset(rightX, waveCenterY + halfBarH),
                strokeWidth = barStroke * 2.2f,
                cap = StrokeCap.Round
            )

            // Crisp bar
            drawLine(
                color = barColor,
                start = Offset(leftX, waveCenterY - halfBarH),
                end = Offset(leftX, waveCenterY + halfBarH),
                strokeWidth = barStroke,
                cap = StrokeCap.Round
            )
            drawLine(
                color = barColor,
                start = Offset(rightX, waveCenterY - halfBarH),
                end = Offset(rightX, waveCenterY + halfBarH),
                strokeWidth = barStroke,
                cap = StrokeCap.Round
            )

            // Small dot above/below shortest outer bars matching reference
            if (i == 0 || i == barHeights.lastIndex) {
                val dotXLeft = if (i == 0) leftX + barSpacing * 0.65f else leftX - barSpacing * 0.7f
                val dotXRight = if (i == 0) rightX - barSpacing * 0.65f else rightX + barSpacing * 0.7f
                if (abs(dotXLeft - cx) > phoneW / 2f + 4.dp.toPx()) {
                    drawCircle(
                        color = ElectricCyan,
                        radius = 2.dp.toPx(),
                        center = Offset(dotXLeft, waveCenterY)
                    )
                    drawCircle(
                        color = ElectricCyan,
                        radius = 2.dp.toPx(),
                        center = Offset(dotXRight, waveCenterY)
                    )
                }
            }
        }

        // Smartphone in center with white play icon
        drawReferenceSmartphone(
            cx = cx,
            phoneTop = phoneTop,
            phoneW = phoneW,
            phoneH = phoneH,
            drawCenterPlayIcon = true
        )
    }
}

@Composable
fun Onboarding2Illustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val h = size.height
        val battW = size.width * 0.30f
        val battH = h * 0.58f
        val battTop = h * 0.19f
        val battLeft = cx - battW / 2f
        val platformY = battTop + battH + h * 0.03f

        // Glowing blue/cyan platform underneath
        drawGlowingPlatform(
            cx = cx,
            platformY = platformY,
            platformWidth = size.width * 0.76f,
            platformHeight = h * 0.17f
        )

        // Soft green & cyan ambient glow behind battery
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    BatteryGreen.copy(alpha = 0.26f),
                    ElectricCyan.copy(alpha = 0.10f),
                    Color.Transparent
                ),
                center = Offset(cx, battTop + battH * 0.5f),
                radius = battH * 0.65f
            ),
            radius = battH * 0.65f,
            center = Offset(cx, battTop + battH * 0.5f)
        )

        // Battery positive terminal cap at top
        val capW = battW * 0.42f
        val capH = 11.dp.toPx()
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFF7D8590), Color(0xFFE6EDF5), Color(0xFF6E7681))
            ),
            topLeft = Offset(cx - capW / 2f, battTop - capH + 2.dp.toPx()),
            size = Size(capW, capH),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Outer metallic battery casing
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFF586069), Color(0xFFB8C2CC), Color(0xFF3B424B))
            ),
            topLeft = Offset(battLeft, battTop),
            size = Size(battW, battH),
            cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
        )

        // Inner dark casing recess
        val rimInset = 4.dp.toPx()
        drawRoundRect(
            color = Color(0xFF0A140D),
            topLeft = Offset(battLeft + rimInset, battTop + rimInset),
            size = Size(battW - rimInset * 2, battH - rimInset * 2),
            cornerRadius = CornerRadius(11.dp.toPx(), 11.dp.toPx())
        )

        // Glowing neon green charge core inside battery
        val coreTop = battTop + battH * 0.11f
        val coreBottom = battTop + battH * 0.90f
        val coreLeft = battLeft + rimInset * 1.8f
        val coreWidth = battW - rimInset * 3.6f
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    BatteryGreenDark,
                    Color(0xFF4EFF61),
                    BatteryGreen,
                    Color(0xFF159926)
                )
            ),
            topLeft = Offset(coreLeft, coreTop),
            size = Size(coreWidth, coreBottom - coreTop),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )

        // Vertical glossy highlight strip on left side of battery
        drawRoundRect(
            color = Color.White.copy(alpha = 0.25f),
            topLeft = Offset(coreLeft + coreWidth * 0.12f, coreTop + 6.dp.toPx()),
            size = Size(coreWidth * 0.12f, (coreBottom - coreTop) - 12.dp.toPx()),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Crisp White Lightning Bolt in center of battery
        val boltCx = cx
        val boltCy = (coreTop + coreBottom) / 2f
        val boltW = coreWidth * 0.44f
        val boltH = (coreBottom - coreTop) * 0.54f
        val lightningPath = Path().apply {
            moveTo(boltCx + boltW * 0.14f, boltCy - boltH * 0.5f)
            lineTo(boltCx - boltW * 0.46f, boltCy + boltH * 0.06f)
            lineTo(boltCx - boltW * 0.04f, boltCy + boltH * 0.06f)
            lineTo(boltCx - boltW * 0.18f, boltCy + boltH * 0.5f)
            lineTo(boltCx + boltW * 0.46f, boltCy - boltH * 0.06f)
            lineTo(boltCx + boltW * 0.05f, boltCy - boltH * 0.06f)
            close()
        }
        drawPath(path = lightningPath, color = TextPrimary)

        // Green Leaf on bottom-right of battery
        val leafBaseX = battLeft + battW * 0.86f
        val leafBaseY = battTop + battH * 0.94f
        val leafTipX = battLeft + battW * 1.48f
        val leafTipY = battTop + battH * 0.40f

        val leafPath = Path().apply {
            moveTo(leafBaseX, leafBaseY)
            cubicTo(
                leafBaseX - battW * 0.08f, leafBaseY - battH * 0.34f,
                leafTipX - battW * 0.34f, leafTipY + battH * 0.02f,
                leafTipX, leafTipY
            )
            cubicTo(
                leafTipX + battW * 0.05f, leafTipY + battH * 0.30f,
                leafBaseX + battW * 0.35f, leafBaseY - battH * 0.04f,
                leafBaseX, leafBaseY
            )
            close()
        }
        drawPath(
            path = leafPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF56FF68), BatteryGreen, Color(0xFF168F25)),
                start = Offset(leafTipX, leafTipY),
                end = Offset(leafBaseX, leafBaseY)
            )
        )

        // Central leaf stem + veins
        val stemPath = Path().apply {
            moveTo(leafBaseX - 4.dp.toPx(), leafBaseY + 5.dp.toPx())
            quadraticTo(
                (leafBaseX + leafTipX) * 0.5f,
                (leafBaseY + leafTipY) * 0.52f,
                leafTipX - 4.dp.toPx(),
                leafTipY + 6.dp.toPx()
            )
        }
        drawPath(
            path = stemPath,
            color = Color(0xFF0E5E18),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun Onboarding3Illustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val h = size.height
        val phoneW = size.width * 0.37f
        val phoneH = h * 0.74f
        val phoneTop = h * 0.08f
        val platformY = phoneTop + phoneH + h * 0.02f

        // Blue glowing platform underneath
        drawGlowingPlatform(
            cx = cx,
            platformY = platformY,
            platformWidth = size.width * 0.74f,
            platformHeight = h * 0.15f
        )

        // Black smartphone outline (no center play icon — play icon is in floating button)
        drawReferenceSmartphone(
            cx = cx,
            phoneTop = phoneTop,
            phoneW = phoneW,
            phoneH = phoneH,
            drawCenterPlayIcon = false
        )

        // Glowing blue circular play button on the right bezel
        val btnCenter = Offset(
            x = cx + phoneW * 0.26f,
            y = phoneTop + phoneH * 0.55f
        )
        val btnRadius = 24.dp.toPx()

        // Outer neon halo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ElectricCyan.copy(alpha = 0.55f),
                    DeepBlue.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = btnCenter,
                radius = btnRadius * 1.8f
            ),
            radius = btnRadius * 1.8f,
            center = btnCenter
        )

        // Deep blue circle fill
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF1A56B8), Color(0xFF0A214A)),
                center = btnCenter,
                radius = btnRadius
            ),
            radius = btnRadius,
            center = btnCenter
        )

        // Glowing cyan border
        drawCircle(
            color = ElectricCyan,
            radius = btnRadius,
            center = btnCenter,
            style = Stroke(width = 2.4.dp.toPx())
        )

        // White Play Triangle inside floating button
        val playR = btnRadius * 0.42f
        val playPath = Path().apply {
            moveTo(btnCenter.x - playR * 0.45f, btnCenter.y - playR * 0.65f)
            lineTo(btnCenter.x - playR * 0.45f, btnCenter.y + playR * 0.65f)
            lineTo(btnCenter.x + playR * 0.75f, btnCenter.y)
            close()
        }
        drawPath(path = playPath, color = TextPrimary)

        // Cyan interaction rays radiating top-right
        val rayAngles = floatArrayOf(-55f, -25f, 5f)
        for (deg in rayAngles) {
            rotate(degrees = deg, pivot = btnCenter) {
                drawLine(
                    color = ElectricCyan,
                    start = Offset(btnCenter.x + btnRadius + 6.dp.toPx(), btnCenter.y),
                    end = Offset(btnCenter.x + btnRadius + 16.dp.toPx(), btnCenter.y),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // White pointing hand illustration tapping the floating button
        val fingerTipX = btnCenter.x + 6.dp.toPx()
        val fingerTipY = btnCenter.y + 8.dp.toPx()
        val handPath = Path().apply {
            // Index finger going up-left toward button
            moveTo(fingerTipX, fingerTipY)
            quadraticTo(
                fingerTipX + 7.dp.toPx(),
                fingerTipY - 4.dp.toPx(),
                fingerTipX + 12.dp.toPx(),
                fingerTipY + 5.dp.toPx()
            )
            // Down index finger right side to second knuckle
            lineTo(fingerTipX + 22.dp.toPx(), fingerTipY + 26.dp.toPx())
            // Middle/ring/pinky folded knuckles
            quadraticTo(
                fingerTipX + 30.dp.toPx(),
                fingerTipY + 21.dp.toPx(),
                fingerTipX + 35.dp.toPx(),
                fingerTipY + 30.dp.toPx()
            )
            quadraticTo(
                fingerTipX + 43.dp.toPx(),
                fingerTipY + 27.dp.toPx(),
                fingerTipX + 47.dp.toPx(),
                fingerTipY + 37.dp.toPx()
            )
            // Right edge of palm down to wrist
            lineTo(fingerTipX + 54.dp.toPx(), fingerTipY + 62.dp.toPx())
            lineTo(fingerTipX + 24.dp.toPx(), fingerTipY + 74.dp.toPx())
            // Left thumb side
            lineTo(fingerTipX + 6.dp.toPx(), fingerTipY + 50.dp.toPx())
            quadraticTo(
                fingerTipX - 8.dp.toPx(),
                fingerTipY + 42.dp.toPx(),
                fingerTipX - 2.dp.toPx(),
                fingerTipY + 34.dp.toPx()
            )
            lineTo(fingerTipX + 8.dp.toPx(), fingerTipY + 36.dp.toPx())
            // Up left side of index finger
            lineTo(fingerTipX - 2.dp.toPx(), fingerTipY + 10.dp.toPx())
            quadraticTo(
                fingerTipX - 4.dp.toPx(),
                fingerTipY + 3.dp.toPx(),
                fingerTipX,
                fingerTipY
            )
            close()
        }
        drawPath(
            path = handPath,
            brush = Brush.linearGradient(
                colors = listOf(Color.White, Color(0xFFD8DEE6), Color(0xFF9BA3AE)),
                start = Offset(fingerTipX, fingerTipY),
                end = Offset(fingerTipX + 45.dp.toPx(), fingerTipY + 70.dp.toPx())
            )
        )
        drawPath(
            path = handPath,
            color = Color(0xFF5A626E),
            style = Stroke(width = 1.2.dp.toPx(), join = StrokeJoin.Round)
        )
    }
}
