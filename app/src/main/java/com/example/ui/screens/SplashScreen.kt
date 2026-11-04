package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.SplashLogoIllustration
import com.example.ui.components.SplashSegmentedSpinner
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    autoAdvance: Boolean = true
) {
    if (autoAdvance) {
        LaunchedEffect(Unit) {
            delay(1800)
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onFinished
            )
            .testTag("splash_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.95f))

            // Centered application logo
            SplashLogoIllustration(
                modifier = Modifier
                    .size(185.dp)
                    .testTag("splash_logo")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // App Name: "Black Screen" / "Overlay"
            Text(
                text = stringResource(R.string.app_title_line1),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 29.sp,
                lineHeight = 33.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.app_title_line2),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 29.sp,
                lineHeight = 33.sp,
                color = ElectricCyan,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thin cyan horizontal divider
            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(1.5.dp)
                    .background(ElectricCyan)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = stringResource(R.string.splash_tagline),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Small circular loading indicator near lower-middle area
            SplashSegmentedSpinner(
                modifier = Modifier
                    .size(26.dp)
                    .testTag("splash_loading_indicator")
            )

            Spacer(modifier = Modifier.weight(1.05f))
        }
    }
}
