package com.adrian.monuver.feature.saving.domain.common

internal sealed class DeleteState {
    data object Idle : DeleteState()
    data class  Progress(val current: Int, val total: Int) : DeleteState()
    data object Success : DeleteState()
    data class  Error(val throwable: Throwable) : DeleteState()
}