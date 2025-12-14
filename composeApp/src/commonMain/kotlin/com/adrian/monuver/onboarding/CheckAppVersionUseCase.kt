package com.adrian.monuver.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrian.monuver.core.domain.common.CheckAppVersionState
import monuver.composeapp.generated.resources.Res
import monuver.composeapp.generated.resources.please_wait_this_process
import monuver.composeapp.generated.resources.process_failed
import monuver.composeapp.generated.resources.refresh
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CheckAppVersionScreen(
    state: CheckAppVersionState,
    onCheck: () -> Unit,
    onSetFirstTimeToFalse: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        var status by remember { mutableStateOf(state) }

        LaunchedEffect(Unit) {
            onCheck()
        }

        LaunchedEffect(state) {
            status = state
        }

        when (status) {
            is CheckAppVersionState.Check -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = stringResource(Res.string.please_wait_this_process),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                    )
                }
            }
            is CheckAppVersionState.Success -> onSetFirstTimeToFalse()
            is CheckAppVersionState.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.process_failed),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                    )
                    OutlinedButton(
                        onClick = onCheck,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = stringResource(Res.string.refresh),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}