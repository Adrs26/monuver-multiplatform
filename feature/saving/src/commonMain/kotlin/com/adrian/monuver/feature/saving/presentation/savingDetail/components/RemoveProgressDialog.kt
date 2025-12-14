package com.adrian.monuver.feature.saving.presentation.savingDetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.data_delete_in_progress
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RemoveProgressDialog(
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
                RemoveProgressAnimation(
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.data_delete_in_progress),
                    modifier = Modifier.padding(24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun RemoveProgressAnimation(
    modifier: Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes("files/delete_progress.json").decodeToString())
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
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}