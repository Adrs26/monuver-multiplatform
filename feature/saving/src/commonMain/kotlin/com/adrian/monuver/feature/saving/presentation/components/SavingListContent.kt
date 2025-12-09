package com.adrian.monuver.feature.saving.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrian.monuver.core.domain.model.Saving
import com.adrian.monuver.core.presentation.util.debouncedClickable
import monuver.feature.saving.generated.resources.Res
import monuver.feature.saving.generated.resources.list_active_save
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SavingListContent(
    totalCurrentAmount: Long,
    savings: List<Saving>,
    onNavigateToSavingDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        item {
            SavingBalanceContent(
                totalCurrentAmount = totalCurrentAmount,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(Res.string.list_active_save),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = savings.size,
            key = { index -> savings[index].id }
        ) { index ->
            SavingListItem(
                saving = savings[index],
                modifier = Modifier.debouncedClickable { onNavigateToSavingDetail(savings[index].id) }
            )
        }
    }
}