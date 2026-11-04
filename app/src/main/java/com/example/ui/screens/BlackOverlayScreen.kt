package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary

@Composable
fun BlackOverlayScreen(
    overlayOpacityPercent: Int,
    floatingButtonEnabled: Boolean,
    onTapToUnlock: () -> Unit,
    onFloatingButtonClick: () -> Unit
) {
    BackHandler(onBack = onFloatingButtonClick)

    val alpha = (overlayOpacityPercent.coerceIn(20, 100) / 100f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack.copy(alpha = alpha))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTapToUnlock
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("black_overlay_screen")
    ) {
        // Bottom area: Centered "Tap to unlock" and Bottom-Right Floating Blue Circular Play Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {
            Text(
                text = stringResource(R.string.tap_to_unlock),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTapToUnlock
                    )
                    .testTag("tap_to_unlock_text")
            )

            if (floatingButtonEnabled) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(56.dp)
                        .clip(CircleShape)
                        .clickable(
                            role = Role.Button,
                            onClick = onFloatingButtonClick
                        )
                        .testTag("overlay_floating_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val c = Offset(size.width / 2f, size.height / 2f)
                        val r = size.minDimension / 2f - 5.dp.toPx()

                        // Outer blue/cyan neon glow
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ElectricCyan.copy(alpha = 0.55f),
                                    DeepBlue.copy(alpha = 0.25f),
                                    Color.Transparent
                                ),
                                center = c,
                                radius = size.minDimension / 2f
                            ),
                            radius = size.minDimension / 2f,
                            center = c
                        )

                        // Deep blue button body
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFF194B9E), Color(0xFF0A2046)),
                                center = c,
                                radius = r
                            ),
                            radius = r,
                            center = c
                        )

                        // Bright cyan ring border
                        drawCircle(
                            color = ElectricCyan,
                            radius = r,
                            center = c,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // White Play Triangle inside
                        val pr = r * 0.44f
                        val playPath = Path().apply {
                            moveTo(c.x - pr * 0.42f, c.y - pr * 0.65f)
                            lineTo(c.x - pr * 0.42f, c.y + pr * 0.65f)
                            lineTo(c.x + pr * 0.72f, c.y)
                            close()
                        }
                        drawPath(path = playPath, color = TextPrimary)
                    }
                }
            }
        }
    }
}
