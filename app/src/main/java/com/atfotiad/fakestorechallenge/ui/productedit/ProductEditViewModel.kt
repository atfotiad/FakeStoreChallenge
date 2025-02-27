package com.atfotiad.fakestorechallenge.ui.productedit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCase
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.getOrError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  [ProductEditViewModel] is a ViewModel that handles the product edit screen.
 *  @param useCase is a FakeStoreUseCase object that contains the use case.
 *  @param context is a Context object that contains the application context.
 *  @property _uiState is a MutableStateFlow object that contains the UI state.
 *  @property uiState is a StateFlow object that contains the UI state.
 *  @property updateProduct is a function that updates the product.
 *  @property ProductEditUiState is a data class that contains the UI state for the product edit screen.
 * */
@HiltViewModel
class ProductEditViewModel @Inject constructor(
    private val useCase: FakeStoreUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductEditUiState())
    val uiState: StateFlow<ProductEditUiState> = _uiState.asStateFlow()

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            useCase.updateProduct(product).getOrError(context)
            _uiState.value = _uiState.value.copy(product = product)
        }
    }
}

data class ProductEditUiState(
    val product: Product? = null
)