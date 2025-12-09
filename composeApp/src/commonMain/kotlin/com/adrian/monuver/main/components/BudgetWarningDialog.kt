package com.adrian.monuver.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adrian.monuver.core.domain.util.BudgetWarningCondition
import com.adrian.monuver.core.presentation.util.DatabaseCodeMapper
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monuver.composeapp.generated.resources.Res
import monuver.composeapp.generated.resources.full_budget_warning
import monuver.composeapp.generated.resources.low_remaining_budget_warning
import monuver.composeapp.generated.resources.over_budget_warning
import monuver.composeapp.generated.resources.yes_i_am_understand
import org.jetbrains.compose.resources.stringResource

@Composable
fun BudgetWarningDialog(
    budgetCategory: Int,
    budgetWarningCondition: Int,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(getAnimation(budgetWarningCondition)
        ).decodeToString())
    }
    val progress by animateLottieCompositionAsState(composition)

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
                modifier = modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberLottiePainter(
                        composition = composition,
                        progress = { progress }
                    ),
                    contentDescription = "Lottie Animation",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = getWarningText(budgetWarningCondition, budgetCategory),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Text(
                        text = stringResource(Res.string.yes_i_am_understand),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

private fun getAnimation(budgetWarningCondition: Int): String {
    return if (budgetWarningCondition == BudgetWarningCondition.LOW_REMAINING_BUDGET) {
        "files/low_remaining_budget.json"
    } else {
        "files/full_budget.json"
    }
}

@Composable
private fun getWarningText(
    budgetWarningCondition: Int,
    budgetCategory: Int
): String {
    return when (budgetWarningCondition) {
        BudgetWarningCondition.LOW_REMAINING_BUDGET -> {
            stringResource(
                Res.string.low_remaining_budget_warning,
                stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetCategory))
            )
        }
        BudgetWarningCondition.FULL_BUDGET -> {
            stringResource(
                Res.string.full_budget_warning,
                stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetCategory))
            )
        }
        else -> {
            stringResource(
                Res.string.over_budget_warning,
                stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetCategory))
            )
        }
    }
}