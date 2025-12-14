package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.common.CheckAppVersionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CheckAppVersionUseCase {
    operator fun invoke(): Flow<CheckAppVersionState> {
        return flow {
            emit(CheckAppVersionState.Check)
            delay(3.seconds)
            emit(CheckAppVersionState.Success)
        }
    }
}