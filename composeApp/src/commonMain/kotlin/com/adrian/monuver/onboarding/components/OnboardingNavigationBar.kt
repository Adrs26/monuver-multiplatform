package com.adrian.monuver.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import monuver.composeapp.generated.resources.Res
import monuver.composeapp.generated.resources.back
import monuver.composeapp.generated.resources.next
import monuver.composeapp.generated.resources.start
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnboardingNavigationBar(
    pagerState: PagerState,
    onNext: () -> Unit,
    onBack: () -> Unit,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == totalPages - 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.weight(0.5f),
            enabled = currentPage > 0
        ) {
            Text(
                text = stringResource(Res.string.back),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (currentPage > 0) MaterialTheme.colorScheme.onSurfaceVariant else
                        MaterialTheme.colorScheme.surface
                )
            )
        }
        PageIndicator(
            totalPages = totalPages,
            currentPage = currentPage,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        TextButton(
            onClick = onNext,
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                text = if (isLastPage) stringResource(Res.string.start) else stringResource(Res.string.next),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun PageIndicator(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            val color = if (index == currentPage) MaterialTheme.colorScheme.primary else
                MaterialTheme.colorScheme.surfaceVariant

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color)
            )
        }
    }
}