package com.adrian.monuver

import androidx.compose.ui.window.ComposeUIViewController
import com.adrian.monuver.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}