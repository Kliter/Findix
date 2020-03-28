package com.kl.findix.util

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    object Loaded: UiState()
    object Retry: UiState()
}