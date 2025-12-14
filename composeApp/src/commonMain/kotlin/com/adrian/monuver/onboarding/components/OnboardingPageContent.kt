package com.adrian.monuver.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.onboarding.OnboardingPage
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monuver.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OnboardingAnimation(
            animation = page.animation,
            fraction = page.fraction,
            modifier = Modifier
                .size(300.dp)
                .padding(
                    start = if (page.isMoved == 1) 12.dp else 0.dp,
                    top = if (page.isMoved == 2) 32.dp else 0.dp
                )
        )
        Spacer(Modifier.height(64.dp))
        Text(
            text = stringResource(page.title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = stringResource(page.description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
        )
    }
}

@Composable
internal fun OnboardingAnimation(
    animation: String,
    fraction: Float,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes(animation).decodeToString())
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress }
            ),
            contentDescription = "Lottie Animation",
            modifier = Modifier.fillMaxSize(fraction)
        )
    }
}