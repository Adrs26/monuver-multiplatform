package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.common.CheckAppVersionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class CheckAppVersionUseCase {
    operator fun invoke(): Flow<CheckAppVersionState> {
        return flow {
            emit(CheckAppVersionState.Check)
            delay(3.seconds)
            emit(CheckAppVersionState.Success)
        }
    }
}