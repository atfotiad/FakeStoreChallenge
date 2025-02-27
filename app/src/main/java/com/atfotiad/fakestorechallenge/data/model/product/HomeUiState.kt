package com.atfotiad.fakestorechallenge.data.model.product
/** [HomeUiState] is a sealed class that represents the state of the home screen.
 * @property Loading is a data object that represents the loading state.
 * @property Success is a data class that represents the success state. It holds the data [HomeData]for the home screen.
 * */
sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val data: HomeData) : HomeUiState()
}