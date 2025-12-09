package com.adrian.monuver.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round

@Composable
fun NumberTextField(
    state: TextFieldState,
    label: String,
    errorMessage: String,
    onValueAsLongCent: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    LaunchedEffect(state.text) {
        val input = state.text.toString()
        val cleaned = input.replace(".", "")

        val formatted = formatNumber(cleaned)
        if (formatted != input) {
            state.edit {
                replace(0, length, formatted)
            }
        }

        val numeric = cleaned.replace(",", ".").toDoubleOrNull()?.times(100)?.toLong()
        onValueAsLongCent(numeric ?: 0)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
        )
        OutlinedTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
            placeholder = {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                )
            },
            supportingText = {
                if (isError) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                    )
                }
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            lineLimits = TextFieldLineLimits.SingleLine,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorSupportingTextColor = MaterialTheme.colorScheme.error
            )
        )
    }
}

private fun formatNumber(raw: String): String {
    if (raw.isEmpty()) return ""

    val regex = Regex("""^\d*(,\d{0,2})?$""")
    if (!raw.matches(regex)) return convertToDouble(raw)

    val parts = raw.split(",")
    val intPart = parts[0]
    val decimalPart = parts.getOrNull(1) ?: ""

    val grouped = intPart.reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    return when {
        raw.endsWith(",") -> "$grouped,"
        decimalPart.isNotEmpty() -> "$grouped,$decimalPart"
        else -> grouped
    }
}

private fun convertToDouble(raw: String): String {
    val tempResult = raw.replace(",", ".").toDoubleOrNull()

    if (tempResult != null) {
        val factor = 100.0
        return (round(tempResult * factor) / factor).toString().replace(".", ",")
    } else {
        return raw
    }
}
