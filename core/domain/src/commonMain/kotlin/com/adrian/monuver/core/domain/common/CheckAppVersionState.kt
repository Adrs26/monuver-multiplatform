package com.adrian.monuver.core.domain.common

sealed class CheckAppVersionState {
    data object Check : CheckAppVersionState()
    data object Success : CheckAppVersionState()
    data object Error : CheckAppVersionState()
}