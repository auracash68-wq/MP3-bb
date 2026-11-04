package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.DarkPillButton
import com.example.ui.components.Onboarding1Illustration
import com.example.ui.components.Onboarding2Illustration
import com.example.ui.components.Onboarding3Illustration
import com.example.ui.components.PrimaryCyanButton
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    pageIndex: Int, // 0, 1, or 2
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onJumpToPage: (Int) -> Unit = {}
) {
    if (pageIndex > 0) {
        BackHandler(onBack = onBack)
    }

    val titlePart1 = when (pageIndex) {
        0 -> stringResource(R.string.onboarding1_title_part1)
        1 -> stringResource(R.string.onboarding2_title_part1)
        else -> stringResource(R.string.onboarding3_title_part1)
    }
    val titleHighlight = when (pageIndex) {
        0 -> stringResource(R.string.onboarding1_title_highlight)
        1 -> stringResource(R.string.onboarding2_title_highlight)
        else -> stringResource(R.string.onboarding3_title_highlight)
    }
    val description = when (pageIndex) {
        0 -> stringResource(R.string.onboarding1_desc)
        1 -> stringResource(R.string.onboarding2_desc)
        else -> stringResource(R.string.onboarding3_desc)
    }

    // Per prompt Section 4, 5, 6 and reference image:
    // Screen 02 (page 0): First dot active in cyan (index 0)
    // Screen 03 (page 1): Third dot active in cyan, matching the reference image (index 2)
    // Screen 04 (page 2): Third dot active in cyan (index 2)
    val activeDotIndex = when (pageIndex) {
        0 -> 0
        1 -> 2
        else -> 2
    }

    val rightButtonText = if (pageIndex == 2) {
        stringResource(R.string.action_get_started)
    } else {
        stringResource(R.string.action_next)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("onboarding_screen_${pageIndex + 1}"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(34.dp))

            // Heading with cyan highlight
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(titlePart1)
                    }
                    withStyle(
                        SpanStyle(
                            color = ElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(titleHighlight)
                    }
                },
                fontFamily = InterFontFamily,
                fontSize = 27.sp,
                lineHeight = 34.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("onboarding_heading")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Supporting Description
            Text(
                text = description,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 21.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("onboarding_description")
            )

            Spacer(modifier = Modifier.weight(0.45f))

            // Center Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(295.dp),
                contentAlignment = Alignment.Center
            ) {
                when (pageIndex) {
                    0 -> Onboarding1Illustration(modifier = Modifier.fillMaxSize())
                    1 -> Onboarding2Illustration(modifier = Modifier.fillMaxSize())
                    else -> Onboarding3Illustration(modifier = Modifier.fillMaxSize())
                }
            }

            Spacer(modifier = Modifier.weight(0.55f))

            // Three Pagination Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("onboarding_pagination_dots")
            ) {
                for (dotIdx in 0..2) {
                    val isDotActive = dotIdx == activeDotIndex
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDotActive) ElectricCyan else Color(0xFF3A3F47)
                            )
                            .clickable { onJumpToPage(dotIdx) }
                            .testTag("onboarding_dot_$dotIdx")
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Bottom Skip & Next / Get Started Pill Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DarkPillButton(
                    text = stringResource(R.string.action_skip),
                    onClick = onSkip,
                    modifier = Modifier.weight(1f),
                    height = 48.dp,
                    testTag = "onboarding_skip_button"
                )
                PrimaryCyanButton(
                    text = rightButtonText,
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    height = 48.dp,
                    testTag = if (pageIndex == 2) "onboarding_get_started_button" else "onboarding_next_button"
                )
            }
        }
    }
}
