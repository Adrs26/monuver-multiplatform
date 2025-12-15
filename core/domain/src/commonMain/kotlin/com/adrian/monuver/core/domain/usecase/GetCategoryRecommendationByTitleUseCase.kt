package com.adrian.monuver.core.domain.usecase

import com.adrian.monuver.core.domain.model.TransactionCategoryRecommendation
import com.adrian.monuver.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class GetCategoryRecommendationByTitleUseCase(
    private val repository: CoreRepository
) {
    operator fun invoke(
        title: String,
        type: Int
    ): Flow<TransactionCategoryRecommendation> {
        return repository.getCategoryRecommendationByTitle(
            title = title,
            type = type
        )
    }
}