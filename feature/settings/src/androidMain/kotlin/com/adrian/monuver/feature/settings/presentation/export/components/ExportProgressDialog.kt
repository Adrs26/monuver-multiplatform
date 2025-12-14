package com.adrian.monuver.feature.settings.presentation.export.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monuver.feature.settings.generated.resources.Res
import monuver.feature.settings.generated.resources.create_report_on_progress
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExportProgressDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExportProgressAnimation(
                    modifier = Modifier.size(250.dp)
                )
                Text(
                    text = stringResource(Res.string.create_report_on_progress),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ExportProgressAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes("files/pdf.json").decodeToString())
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
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
            modifier = Modifier.fillMaxSize(0.9f)
        )
    }
}