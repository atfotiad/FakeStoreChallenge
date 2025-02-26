package com.atfotiad.fakestorechallenge.data.model.product

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val data: HomeData) : HomeUiState()
}