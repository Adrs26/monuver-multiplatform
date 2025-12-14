package com.adrian.monuver.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.adrian.monuver.onboarding.components.OnboardingNavigationBar
import com.adrian.monuver.onboarding.components.OnboardingPageContent
import kotlinx.coroutines.launch
import monuver.composeapp.generated.resources.Res
import monuver.composeapp.generated.resources.onboarding_first_description
import monuver.composeapp.generated.resources.onboarding_first_title
import monuver.composeapp.generated.resources.onboarding_fourth_description
import monuver.composeapp.generated.resources.onboarding_fourth_title
import monuver.composeapp.generated.resources.onboarding_second_description
import monuver.composeapp.generated.resources.onboarding_second_title
import monuver.composeapp.generated.resources.onboarding_third_description
import monuver.composeapp.generated.resources.onboarding_third_title
import org.jetbrains.compose.resources.StringResource

@Composable
internal fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { OnboardingPage.entries.size })

    Scaffold(
        bottomBar = {
            OnboardingNavigationBar(
                pagerState = pagerState,
                totalPages = OnboardingPage.entries.size,
                onNext = {
                    if (pagerState.currentPage == OnboardingPage.entries.size - 1) {
                        onFinishOnboarding()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                onBack = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ) { index ->
            OnboardingPageContent(
                page = OnboardingPage.entries[index]
            )
        }
    }
}

internal enum class OnboardingPage(
    val title: StringResource,
    val description: StringResource,
    val animation: String,
    val fraction: Float,
    val isMoved: Int = 0
) {
    FIRST(
        title = Res.string.onboarding_first_title,
        description = Res.string.onboarding_first_description,
        animation = "files/transaction.json",
        fraction = 0.9f
    ),
    SECOND(
        title = Res.string.onboarding_second_title,
        description = Res.string.onboarding_second_description,
        animation = "files/budget.json",
        fraction = 0.7f,
        isMoved = 1
    ),
    THIRD(
        title = Res.string.onboarding_third_title,
        description = Res.string.onboarding_third_description,
        animation = "files/chart.json",
        fraction = 0.7f,
        isMoved = 2
    ),
    FOURTH(
        title = Res.string.onboarding_fourth_title,
        description = Res.string.onboarding_fourth_description,
        animation = "files/checklist.json",
        fraction = 0.8f
    )
}