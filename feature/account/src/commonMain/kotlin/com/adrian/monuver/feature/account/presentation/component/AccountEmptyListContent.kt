package com.adrian.monuver.feature.account.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monuver.feature.account.generated.resources.Res
import monuver.feature.account.generated.resources.your_list_account
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountEmptyListContent(
    balance: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AccountBalanceContent(
            balance = balance,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(Res.string.your_list_account),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        BudgetingEmptyAnimation(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun BudgetingEmptyAnimation(
    modifier: Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(Res.readBytes("files/wallet.json").decodeToString())
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
            modifier = Modifier.fillMaxSize(fraction = 0.6f),
            contentDescription = "Lottie Animation"
        )
    }
}